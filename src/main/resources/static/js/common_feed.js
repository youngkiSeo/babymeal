function getDateTimeInfo(dt) {
    const nowDt = new Date();
    const targetDt = new Date(dt);

    const nowDtSec = parseInt(nowDt.getTime() / 1000);
    const targetDtSec = parseInt(targetDt.getTime() / 1000);

    const diffSec = nowDtSec - targetDtSec;
    if(diffSec < 120) {
        return '1분 전';
    } else if(diffSec < 3600) { //분 단위 (60 * 60)
        return `${parseInt(diffSec / 60)}분 전`;
    } else if(diffSec < 86400) { //시간 단위 (60 * 60 * 24)
        return `${parseInt(diffSec / 3600)}시간 전`;
    } else if(diffSec < 2592000) { //일 단위 (60 * 60 * 24 * 30)
        return `${parseInt(diffSec / 86400)}일 전`;
    }
    return targetDt.toLocaleString();
}

//프로필 화면으로 이동
function moveToProfile(iuser) {
    location.href = `/user/profile?iuser=${iuser}`;
}

const feedObj = {
    limit: 5,
    itemLength: 0,
    currentPage: 1,
    url: '',
    iuser: 0,
    swiper: null,
    containerElem: document.querySelector('#item_container'),
    loadingElem: document.querySelector('.loading'),
    refreshList: function() {
        this.currentPage = 1;
        this.containerElem.innerHTML = null;
        this.getFeedList(this.currentPage);
    },
    makeFeedList: function(data) {
        if(data.length == 0) { return; }

        for(let i=0; i<data.length; i++) {
            const item = data[i];

            const itemContainer = document.createElement('div');
            itemContainer.className = 'item mt-3 mb-3';

            const regDtInfo = getDateTimeInfo(item.createdAt);
            const topDiv = document.createElement('div');
            topDiv.className = 'd-flex flex-row ps-3 pe-3';

            // 글쓴이 정보 영역
            let imgSrc = '/img/defaultProfileImg.png';
            if(item.pic) {
                imgSrc = item.pic.startsWith('http') ? item.pic : `/pic/profile/${item.iuser}/${item.pic}`;
            }
            const imgTag = `<img src="${imgSrc}" class="pointer profile w30 h30" 
            onclick="moveToProfile(${item.iuser});" onerror="this.onerror=null; this.src='/img/defaultProfileImg.png'">`;
            topDiv.innerHTML = `
                <div class="d-flex flex-column justify-content-center">${imgTag}</div>
                <div class="p-3 flex-grow-1">
                    <div><span class="pointer" onclick="moveToProfile(${item.iuser});">${item.unm}</span> - ${regDtInfo}</div>
                    <div>${item.location == null ? '' : item.location}</div>
                </div>
            `;

            //이미지영역
            const imgSwiperDiv = document.createElement('div');
            imgSwiperDiv.className = 'swiper item_img';
            imgSwiperDiv.innerHTML = `
                <div class="swiper-wrapper"></div>
                <div class="swiper-pagination"></div>
                <div class="swiper-button-prev"></div>
                <div class="swiper-button-next"></div>
            `;

            const swiperWrapperElem = imgSwiperDiv.querySelector('.swiper-wrapper');

            for(let z=0; z<item.imgList.length; z++) {
                const imgObj = item.imgList[z];

                const swiperSlideDiv = document.createElement('div');
                swiperSlideDiv.classList.add('swiper-slide');

                const img = document.createElement('img');
                img.className = 'w614';
                img.src = `/pic/feed/${item.ifeed}/${imgObj.pic}`;
                swiperSlideDiv.append(img);
                swiperWrapperElem.append(swiperSlideDiv);
            }

            itemContainer.append(topDiv);
            itemContainer.append(imgSwiperDiv);

            //좋아요, dm 영역
            const btnsDiv = document.createElement('div');
            btnsDiv.className = 'favCont p-2 d-flex flex-row';

            const heartIcon = document.createElement('i');
            heartIcon.className = 'fa-heart pointer rem1_2 me-3';
            if(item.isFav === 1) { //좋아요 O
                heartIcon.classList.add('fas');
            } else { //좋아요 X
                heartIcon.classList.add('far');
            }
            heartIcon.addEventListener('click', ()=> {
                item.isFav = 1 - item.isFav;
                const access_token = window.localStorage.getItem("access_token")
                fetch(`/api/v1/feed/${item.ifeed}/fav?type=${item.isFav}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${access_token}`
                    }
                })
                    .then(res => res.json())
                    .then(myJson => {
                        if(myJson === 1) {
                            const favCnt = Number(favCntHiddenInput.value);
                            switch (item.isFav) {
                                case 0: //O > X
                                    heartIcon.classList.remove('fas');
                                    heartIcon.classList.add('far');
                                    favCntHiddenInput.value = favCnt - 1;
                                    break;
                                case 1: //X > O
                                    heartIcon.classList.remove('far');
                                    heartIcon.classList.add('fas');
                                    favCntHiddenInput.value = favCnt + 1;
                                    break;
                            }
                            //일부로 이벤트 발생!!!!
                            favCntHiddenInput.dispatchEvent(new Event("change"));
                        }
                    });
            });

            const dmDiv = document.createElement('div');
            dmDiv.className = 'pointer';
            dmDiv.addEventListener('click', ()=> {
               location.href=`/dm?oppoiuser=${item.iuser}`;
            });
            dmDiv.innerHTML = `<svg aria-label="게시물 공유" class="_8-yf5 " color="#262626" fill="#262626" height="24" role="img" viewBox="0 0 24 24" width="24"><line fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="2" x1="22" x2="9.218" y1="3" y2="10.083"></line><polygon fill="none" points="11.698 20.334 22 3.001 2 3.001 9.218 10.084 11.698 20.334" stroke="currentColor" stroke-linejoin="round" stroke-width="2"></polygon></svg>`;

            btnsDiv.append(heartIcon);
            btnsDiv.append(dmDiv);
            itemContainer.append(btnsDiv);

            const favDiv = document.createElement('div');
            favDiv.className = 'p-2 display-none';
            const heartCntSpan = document.createElement('span');
            heartCntSpan.className = 'bold';
            heartCntSpan.innerText = `좋아요 ${item.favCnt}개`;
            favDiv.append(heartCntSpan);
            itemContainer.append(favDiv);

            if(item.favCnt > 0) {
                favDiv.classList.remove('display-none');
            }

            //좋아요 수를 저장하고 있는 hidden input
            const favCntHiddenInput = document.createElement('input');
            favCntHiddenInput.type = 'hidden';
            favCntHiddenInput.value = item.favCnt;
            favCntHiddenInput.addEventListener('change', function(e) {
                if(e.target.value == '0') {
                    favDiv.classList.add('display-none');
                } else {
                    favDiv.classList.remove('display-none');
                    heartCntSpan.innerText = `좋아요 ${e.target.value}개`;
                }
            });
            itemContainer.append(favCntHiddenInput);

            if(item.ctnt != null) { // 글내용 영역
                const ctntDiv = document.createElement('div');
                ctntDiv.innerText = item.ctnt;
                ctntDiv.className = 'itemCtnt p-2';
                itemContainer.append(ctntDiv);
            }

            //댓글 영역
            const cmtDiv = document.createElement('div');
            const cmtListDiv = document.createElement('div');
            const cmtFormDiv = document.createElement('div');
            cmtFormDiv.className = 'p-2 d-flex flex-row div-top';

            cmtDiv.append(cmtListDiv);
            if(item.cmt != null && item.cmt.isMore === 1) {
                const moreCmtDiv = document.createElement('div');
                const moreCmtSpan = document.createElement('span');
                moreCmtSpan.className = 'pointer';
                moreCmtSpan.innerText = '댓글 더보기';
                moreCmtSpan.addEventListener('click', () => {
                    moreCmtSpan.remove();

                    const access_token = window.localStorage.getItem("access_token")
                    fetch(`/api/v1/feed/${item.ifeed}/cmt`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${access_token}`
                        }
                    })
                        .then(res => res.json())
                        .then(result => {
                            cmtListDiv.innerHTML = null;
                            result.forEach(obj => {
                                const cmtItemContainerDiv = this.makeCmtItem(obj);
                                cmtListDiv.append(cmtItemContainerDiv);
                            })
                        });
                });
                moreCmtDiv.append(moreCmtSpan);
                cmtDiv.append(moreCmtDiv);
            }
            cmtDiv.append(cmtFormDiv);

            const cmtInput = document.createElement('input');
            cmtInput.type = 'text';
            cmtInput.placeholder = '댓글을 입력하세요...';
            cmtInput.className = 'flex-grow-1 my_input back_color';
            cmtInput.addEventListener('keyup', (e) => {
                if(e.key === 'Enter') {
                    cmtBtn.click();
                }
            });

            if(item.cmt != null) { //댓글 있음
                const cmtItemContainerDiv = this.makeCmtItem(item.cmt);
                cmtListDiv.append(cmtItemContainerDiv);
            }

            const cmtBtn = document.createElement('button');
            cmtBtn.type = 'button';
            cmtBtn.innerText = '등록';
            cmtBtn.className = 'btn btn-outline-primary';
            cmtBtn.addEventListener('click', () => {
                const cmt = cmtInput.value;
                if(cmt.length === 0) {
                    alert('댓글 내용을 작성해 주세요.');
                    return;
                }

                const param = {
                    ifeed: item.ifeed,
                    cmt: cmt
                }

                const access_token = window.localStorage.getItem("access_token")
                fetch('/api/v1/feed/cmt', {
                    method: 'POST',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${access_token}`
                    },
                    body: JSON.stringify(param)
                })
                    .then(res => res.json())
                    .then(myJson => {
                        console.log(myJson);
                        if(myJson) {
                            const cmtItemDiv = this.makeCmtItem(myJson);
                            cmtListDiv.append(cmtItemDiv);

                            cmtInput.value = '';
                        } else {
                            alert('댓글을 등록할 수 없습니다.');
                        }
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });

            cmtFormDiv.append(cmtInput);
            cmtFormDiv.append(cmtBtn);

            itemContainer.append(cmtDiv);
            this.containerElem.append(itemContainer);
        }
        if(this.swiper != null) { this.swiper = null; }
        this.swiper = new Swiper('.swiper', {
            navigation: {
                nextEl: ".swiper-button-next",
                prevEl: ".swiper-button-prev",
            },
            pagination: {
                el: ".swiper-pagination",
            },
            allowTouchMove: false,
            direction: 'horizontal',
            loop: false,
        });
    },
    setScrollInfinity: function(target) {
        target.addEventListener('scroll', () => {
            const {
                scrollTop,
                scrollHeight,
                clientHeight
            } = document.documentElement;

            if (scrollTop + clientHeight >= scrollHeight - 5 && this.itemLength === this.limit) {
                this.itemLength = 0;
                this.getFeedList(++this.currentPage);
            }
        }, { passive: true });
    },
    getFeedList: function(page) {
        this.showLoading();

        const param = {
            'page': page,
            'limit': this.limit
        }

        if(this.iuser) {
            param.iuserForMyFeed = this.iuser;
        }

        myFetch.get(this.url, myJson => {
            console.log(myJson);
            this.itemLength = myJson.length;
            this.makeFeedList(myJson);
            this.hideLoading();
        }, param);

    },
    makeCmtItem: function({iuser, writerImg, writer, cmt}) {
        const cmtItemContainerDiv = document.createElement('div');
        cmtItemContainerDiv.className = 'cmtItemCont';

        //프로필
        const cmtItemProfileDiv = document.createElement('div');
        cmtItemProfileDiv.className = 'cmtItemProfile';
        const cmtItemWriterProfileImg = document.createElement('img');
        cmtItemWriterProfileImg.src = `/img/defaultProfileImg.png`;
        if(writerImg) {
            cmtItemWriterProfileImg.src = `/pic/profile/${iuser}/${writerImg}`;
        }
        cmtItemWriterProfileImg.className = 'profile w24 pointer';
        cmtItemWriterProfileImg.addEventListener('click', () => {
            moveToProfile(iuser);
        });

        cmtItemProfileDiv.append(cmtItemWriterProfileImg);
        cmtItemContainerDiv.append(cmtItemProfileDiv);

        //댓글
        const cmtItemCtntDiv = document.createElement('div');
        cmtItemCtntDiv.className = 'cmtItemCtnt';
        cmtItemCtntDiv.innerHTML = `<div class="pointer" onclick="moveToProfile(${iuser});">${writer}</div><div>${cmt}</div>`;
        cmtItemContainerDiv.append(cmtItemCtntDiv);

        return cmtItemContainerDiv;
    },
    hideLoading: function() { this.loadingElem.classList.add('display-none');},
    showLoading: function() { this.loadingElem.classList.remove('display-none'); }
}