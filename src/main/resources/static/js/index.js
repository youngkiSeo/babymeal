const msg = {
    isDel: '삭제하시겠습니까?',
    fnIsDel : function(target) {
        return `${target}을(를) ` + this.isDel;
    }
};

//정규식 테스트 사이트
//https://www.regextester.com/

const regex = {
    id: /^([a-zA-Z0-9]{4,15})$/,        //대소문자_숫자조합으로 4~15글자
    pw: /^([a-zA-Z0-9!@_]{4,20})$/,     //대소문자+숫자+!@_ 조합으로 4~20글자
    nm: /^([가-힣]{2,5})$/,             //한글조합으로 2~5글자
    ctnt: /^[^><]*$/,
    msg: {
        id: '대소문자_숫자조합으로 4~15글자',
        pw: '대소문자+숫자+!@_ 조합으로 4~20글자',
        nm: '한글조합으로 2~5글자',
        ctnt: '<, >는 사용할 수 없습니다.',
    },
    isWrongWith: function(target, val) {
        return (target && val) ? !this[target].test(val) : true;
    }
};

const myFetch = {
    send: function(fetchObj, cb) {
        return fetchObj
            .then(res => res.json())
            .then(cb)
            .catch(e => { console.log(e) });
    },
    get: function(url, cb, param) {

        if(param) {
            const queryString = '?' + Object.keys(param).map(key => `${key}=${param[key]}`).join('&');
            url += queryString;
        }
        const headers = {
            'Content-Type': 'application/json',
        };
        const access_token = window.localStorage.getItem("access_token");
        if(access_token) {
            headers.Authorization = `Bearer ${access_token}`;
        }
        return this.send(fetch(url, {
            method: 'GET',
            headers
        }), cb);
    },
    post: function(url, cb, param) {
        const access_token = window.localStorage.getItem("access_token")
        return this.send(fetch(url, {
            'method': 'post',
            'headers': { 'Content-Type': 'application/json', 'Authorization': `Bearer ${access_token}` },
            'body': JSON.stringify(param)
        }), cb);
    },
    put: function(url, cb, param) {
        const access_token = window.localStorage.getItem("access_token")
        return this.send(fetch(url, {
            'method': 'put',
            'headers': { 'Content-Type': 'application/json', 'Authorization': `Bearer ${access_token}` },
            'body': JSON.stringify(param)
        }), cb)
    },
    delete: function(url, cb) {
        const access_token = window.localStorage.getItem("access_token");
        return this.send(fetch(url, {
            'method': 'delete',
            'headers': { 'Content-Type': 'application/json', 'Authorization': `Bearer ${access_token}` },
        }), cb);
    }
}

const newFeedModalBtnElem = document.querySelector('#newFeedModalBtn');
if(newFeedModalBtnElem) {
    const modal = document.querySelector('#newFeedModal');
    const body =  modal.querySelector('#id-modal-body');
    const frmElem = modal.querySelector('form');

    //이미지 값이 변하면
    frmElem.imgs.addEventListener('change', function(e) {

        if(e.target.files.length > 0) {
            body.innerHTML = `
                <div>
                    <div class="d-flex flex-md-row">
                        <div class="flex-grow-1 h-full"><img id="id-img"></div>
                        <div class="ms-1 w250 d-flex flex-column">                
                            <textarea placeholder="문구 입력..." class="flex-grow-1 p-1"></textarea>
                            <input type="text" placeholder="위치" class="mt-1 p-1">
                        </div>
                    </div>
                </div>
                <div class="mt-2">
                    <button type="button" class="btn btn-primary">공유하기</button>
                </div>
            `;
            const imgElem = body.querySelector('#id-img');

            const imgSource = e.target.files[0];
            const reader = new FileReader();
            reader.readAsDataURL(imgSource);
            reader.onload = function() {
              imgElem.src = reader.result;
            };

            const shareBtnElem = body.querySelector('button');
            shareBtnElem.addEventListener('click', function() {
                const files = frmElem.imgs.files;

                const fData = new FormData();
                for(let i=0; i<files.length; i++) {
                    fData.append('imgs', files[i]);
                }
                fData.append('ctnt', body.querySelector('textarea').value);
                fData.append('location', body.querySelector('input[type=text]').value);

                const headers = {};
                const access_token = window.localStorage.getItem("access_token");
                if(access_token) {
                    headers.Authorization = `Bearer ${access_token}`;
                }

                fetch('/api/v1/feed', {
                    method: 'post',
                    headers,
                    body: fData
                }).then(res => res.json())
                    .then(myJson => {

                        const closeBtn = modal.querySelector('.btn-close');
                        closeBtn.click();

                        if(feedObj && myJson) {
                            feedObj.refreshList();
                        }
                    });
            });
        }
    });

    newFeedModalBtnElem.addEventListener('click', function() {
        const selFromComBtn = document.createElement('button');
        selFromComBtn.type = 'button';
        selFromComBtn.className = 'btn btn-primary';
        selFromComBtn.innerText = '컴퓨터에서 선택';
        selFromComBtn.addEventListener('click', function() {
            frmElem.imgs.click();
        });
        body.innerHTML = null;
        body.appendChild(selFromComBtn);
    });
}


