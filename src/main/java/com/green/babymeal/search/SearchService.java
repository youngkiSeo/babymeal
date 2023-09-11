package com.green.babymeal.search;

import com.green.babymeal.common.config.EnToKo.EnToKo;
import com.green.babymeal.common.config.security.AuthenticationFacade;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.repository.ProductRepository;
import com.green.babymeal.search.model.*;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import scala.collection.Seq;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class SearchService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationFacade USERPK;
    private final TaskScheduler taskScheduler;
    private final ProductRepository productRep;
    private final SearchMapper mapper;
    final int keysize = 5;

    public List<SearchPopularVo> list(){
        String key = "a:babymeal";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 4);  //score순으로 10개 보여줌
        return typedTuples.stream().map(item-> SearchPopularVo.builder().product(item.getValue()).count(item.getScore()).build()).toList();
    }

    public List<String> GetRecentSearch() {
        UserEntity loginUser = USERPK.getLoginUser();
        String key = "a:babymeal" +loginUser.getIuser();
        int start = 0;
        List<String> range = redisTemplate.opsForList().range(key, start, keysize);
        return range;
    }

    public Long deleteRecentSearch(String product){
        UserEntity loginUser = USERPK.getLoginUser();
        String key = "a:babymeal" +loginUser.getIuser();
        Long remove = redisTemplate.opsForList().remove(key, 0, product);
        return remove;
    }
    public Long deleteRecentSearchAll(){
        UserEntity loginUser = USERPK.getLoginUser();
        String key = "a:babymeal" +loginUser.getIuser();
        int start = 0;
        List<String> range = redisTemplate.opsForList().range(key, start, keysize);
        Long remove = null;
        for (String keyword:range) {
             remove = redisTemplate.opsForList().remove(key, 0, keyword);
        }


        return remove;
    }

    public Double deleteRedisPopular(String msg){
        Double babymeal = null;
        try{
            babymeal = redisTemplate.opsForZSet().incrementScore("a:babymeal", msg, -1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return babymeal;

    }

    public SearchSelRes selfilter(String product, int page, int row, String sorter, List<String>filter){

        if (sorter==null){
            sorter="0";
        }
        if (null==filter){
            filter = new ArrayList<>();
            filter.add("0");
        }

        StringBuffer allergy = new StringBuffer();
        String strallergy = "";
        String plus="";

        if (filter.size()> 1){
            for (int i = 0; i < filter.size()-1; i++) {
                allergy.append(filter.get(i)+",");
            }
        }
        if (filter.size()>0){
            allergy.append(filter.get(filter.size()-1));
        }

        strallergy = String.valueOf(allergy);

        SearchSelDto dto = new SearchSelDto();
        dto.setPage(page);
        dto.setRow(row);
        dto.setAllergy(strallergy);
        dto.setSorter(Integer.parseInt(sorter));

        int startIdx = (dto.getPage() - 1) * dto.getRow();
        dto.setStartIdx(startIdx);

        String msg = "";
        //영한 변환
        boolean isEnglish = true;

        Pattern p = Pattern.compile("[a-zA-Z0-9]");
        String typoText = product;
        Matcher m = p.matcher(typoText);
        isEnglish = m.find();
        if(isEnglish) {
            msg = EnToKo.engToKor(typoText);
        } else {
            msg = typoText;
        }

        //인기검색어 - 레디스저장
        redispopular(msg);

        //최근검색어 - 레디스저장
        Long loginUser = USERPK.getLoginUser().getIuser();
        if (loginUser!=null){
            log.info("product:{}",product);
            redisrecent(product);
        }

        //트위터 형태소 분석기

        CharSequence normalized = TwitterKoreanProcessorJava.normalize(msg);
        Seq<KoreanTokenizer.KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(normalized);
        Seq<KoreanTokenizer.KoreanToken> stemmed = TwitterKoreanProcessorJava.stem(tokens);
        List<String> text = TwitterKoreanProcessorJava.tokensToJavaStringList(stemmed);

        StringBuffer sb = new StringBuffer();

        if ( text.size() > 0){
            for (int i = 1; i <text.size()-1; i++) {
                sb.append(text.get(i)).append("|");
            }
        }
        sb.append(text.get(text.size()-1));
        dto.setWord(text.get(0).toString());
        dto.setMsg(String.valueOf(sb));


        List<SearchSelProductDto> productDto = mapper.selfilter(dto);


        for (int i = 0; i <productDto.size(); i++) {
            //String thumbnail = productDto.get(i).getImg();
            //int productid = productDto.get(i).getProductid();
            //String fullPath = "/img/product/"+productid+"/"+thumbnail;
            //productDto.get(i).setImg(fullPath);

            String cateId = productDto.get(i).getCateId();
            String name = productDto.get(i).getName();
            StringBuffer productname = new StringBuffer();
            productname.append("[").append(cateId).append("단계] ").append(name);
            productDto.get(i).setName(String.valueOf(productname));
        }

        List<SearchSelproduct> list = new LinkedList<>();

        for (int i = 0; i <productDto.size(); i++) {
            SearchSelproduct selproduct = new SearchSelproduct();
            selproduct.setProductid(productDto.get(i).getProductid());
            selproduct.setName(productDto.get(i).getName());
            selproduct.setThumbnail(productDto.get(i).getImg());
            selproduct.setPrice(productDto.get(i).getPrice());
            list.add(selproduct);
        }

        int num = mapper.maxpage(text.get(0).toString(),String.valueOf(sb), String.valueOf(allergy));
        int maxpage = (int) Math.ceil((double) num / row);

        SearchSelRes selres = new SearchSelRes();
        selres.setDto(list);
        selres.setCount(num);
        selres.setMaxpage(maxpage);
        return selres;

    }


    //인기검색어 저장하는 메소드
    public Double redispopular(String msg){
        Double babymeal = null;
        try{
             babymeal = redisTemplate.opsForZSet().incrementScore("a:babymeal", msg, 1);
        }catch (Exception e) {
            e.printStackTrace();
        }

        Runnable task = () -> {
            redisTemplate.opsForZSet().incrementScore("a:babymeal", msg, -1);
        };

        taskScheduler.schedule(task, Date.from(Instant.now().plus(24, ChronoUnit.HOURS)));

        return babymeal;
    }

    //최근검색어 저장하는 메소드
    public List<String> redisrecent(String product) {
        Long loginUser = USERPK.getLoginUser().getIuser();
        String key = "a:babymeal" +loginUser;

        //레디스에 중복된 단어를 저장 하지 못하도록 하자

        List<String> check = redisTemplate.opsForList().range(key, 0, keysize);

        for (int i = 0; i <check.size(); i++) {
            String redisproduct = check.get(i);
            if (redisproduct.equals(product)){
                return check;
            }
        }

        //레디스에 5개(keysize) 이상 저장 하지 못하도록 하자
        Long size = redisTemplate.opsForList().size(key);
        if (size == keysize) {
            redisTemplate.opsForList().rightPop(key);
        }
        Long result = redisTemplate.opsForList().leftPush(key, product);

        //검색
        List<String> list = redisTemplate.opsForList().range(key, 0, keysize);

        return list;

    }

}
