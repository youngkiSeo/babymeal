package com.green.babymeal.search;

import com.green.babymeal.common.config.EnToKo.EnToKo;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class SearchService {

    private final RedisTemplate<String, String> redisTemplate;
    private final TaskScheduler taskScheduler;
    private final ProductRepository productRep;
    private final SearchMapper mapper;

    public Double search(String product){

        Double babymeal = null;
        try{
            babymeal = redisTemplate.opsForZSet().incrementScore("babymeal", product, 1);
            //redisTemplate.opsForZSet().incrementScore("ranking", keyword, 1, TimeUnit.MILLISECONDS);
        }catch (Exception e) {
            System.out.println(e.toString());
        }

        Runnable task = () -> {
            redisTemplate.opsForZSet().incrementScore("babymeal", product, -1);
        };
        taskScheduler.schedule(task, Date.from(Instant.now().plus(30, ChronoUnit.SECONDS)));

        //scheduledTask = taskScheduler.schedule(this::executeTask, Date.from(Instant.now().plus(24, ChronoUnit.HOURS)));
        return babymeal;
    }





    public List<SearchPopularVo> list(){
        String key = "babymeal";
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 9);  //score순으로 10개 보여줌
        return typedTuples.stream().map(item-> SearchPopularVo.builder().product(item.getValue()).count(item.getScore()).build()).toList();
    }

    public SearchSelRes selfilter(String product, int page, int row, int sorter, List<String>filter){
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
        dto.setSorter(sorter);


        int startIdx = (dto.getPage() - 1) * dto.getRow();
        dto.setStartIdx(startIdx);



        String msg = "";

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

        try{
             redisTemplate.opsForZSet().incrementScore("babymeal", msg, 1);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String finalMsg = msg;
        Runnable task = () -> {
            redisTemplate.opsForZSet().incrementScore("babymeal", finalMsg, -1);
        };

        taskScheduler.schedule(task, Date.from(Instant.now().plus(10, ChronoUnit.SECONDS)));

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


        List<SearchSelVo> productDto = mapper.selfilter(dto);


        for (int i = 0; i <productDto.size(); i++) {
            String thumbnail = productDto.get(i).getImg();
            int productid = productDto.get(i).getProductid();
            String fullPath =thumbnail;
            productDto.get(i).setImg(fullPath);

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



}
