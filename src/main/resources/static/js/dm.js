const sock = new SockJS('/ws-dm');
const ws = Stomp.over(sock);
let globalIdm = 0;
ws.connect({}, function() {
    console.log('STOMP Connection !!');
    if(msgParam.idm) {
        connect(msgParam.idm);
    }
});

function connect(idm) {
    Object.keys(ws.subscriptions).forEach(function(item) {
        ws.unsubscribe(item);//이전 연결된 구독이 있으면 제거
    })
    globalIdm = idm;
    ws.subscribe(`/sub/room/${idm}`, onMessage);
}

function sendMsg(msg) {
    if(ws) {
        ws.send('/pub/msg', {}, JSON.stringify({ idm:globalIdm, msg: msg}))
    }
}

function onMessage(msg) {
    const item = JSON.parse(msg.body);
    const div = makeDmMsgItem(item);
    dmMsgContainerElem.append(div);
    dmMsgContainerElem.scrollTop = dmMsgContainerElem.scrollHeight;
}

const globalConstElem = document.querySelector('#globalConst');
const localConstElem = document.querySelector('#localConst');
const oppoiuser = localConstElem.dataset.oppoiuser;
const dmUserListContainerElem = document.querySelector('.dm_user_list_container');

//사용자 리스트 가져오기
function getDmUserList() {
    myFetch.get('/dm/list', myJson => {
        makeDmUserList(myJson);
    });
}
function makeDmUserList(userList) {
    let isNonExistent = true;
    userList.forEach(function(item) {
        const div = makeDmUserItem(item);
        dmUserListContainerElem.append(div);

        if(oppoiuser == item.opponent.iuser) {
            isNonExistent = false;
            getDmMsgList(item.idm);
            console.log(ws);
            /*
            ws.ws.onopen = function(e) {
                connect(item.idm);
            }
            */
        }
    });

    if(isNonExistent) {
        myFetch.get('/dm/reg', myJson => {
            const div = makeDmUserItem(myJson);
            dmUserListContainerElem.append(div);

            connect(myJson.idm);
        }, {
            toiuser: oppoiuser
        });
    }
}
function makeDmUserItem(item) {
    const div = document.createElement('div');
    div.className = 'pointer d-flex flex-row w-full'
    div.innerHTML = `
        <div class="w100">
            <img src="/pic/profile/${item.opponent.iuser}/${item.opponent.mainimg}" class="profile w30 h30" 
             onerror="this.onerror=null; this.src='/img/defaultProfileImg.png'">
        </div>
        <div class="flex-grow-1">
            <div>${item.opponent.nm}</div>
            <div>${item.lastmsg == null ? '&nbsp;' : item.lastmsg}</div>
        </div>
    `;
    div.addEventListener('click', function() {
        dmMsgContainerElem.innerHTML = null;
        connect(item.idm);
        isFirst = true;
        msgParam.page = 0;
        msgParam.isNoMore = false;
        getDmMsgList(item.idm);
    })
    return div;
}
getDmUserList();

const globalIuser = globalConstElem.dataset.iuser;
const dmMsgContainerElem = document.querySelector('.dm_msg_container');
const msgInput = document.querySelector('#msg_input');
const dmMsgSendBtn = document.querySelector('#button-send');

dmMsgContainerElem.addEventListener('scroll', (e) => {
    if(e.target.scrollTop === 0) {
        getDmMsgList();
    }
})

msgInput.addEventListener('keyup', (e) => {
    if(e.key === 'Enter') {
        dmMsgSendBtn.click();
    }
});

dmMsgSendBtn.addEventListener('click', function() {
    if(globalIdm && msgInput.value) {
        sendMsg(msgInput.value);
        msgInput.value = '';
    }
});

// 메세지 리스트 가져오기
const msgParam = {
    isNoMore: false,
    idm: 0,
    page: 0,
    limit: 50
}
let isFirst = true;

function getDmMsgList(idm) {
    isFirst = false;
    if(msgParam.isNoMore) { return; }
    if(idm) {
        msgParam.idm = idm;
    }
    msgParam.page++;
    msgParam.isNoMore = true;
    myFetch.get('/dm/msg/list', myJson => {
        if(myJson.length > 0) {
            if(myJson.length === msgParam.limit) {
                msgParam.isNoMore = false;
            }
            makeDmMsgList(myJson);
        }
    }, msgParam);
}

function makeDmMsgList(msgList) {
    msgList.forEach(function(item) {
        const div = makeDmMsgItem(item);
        dmMsgContainerElem.prepend(div);
    });

    if(isFirst) {
        dmMsgContainerElem.scrollTop = dmMsgContainerElem.scrollHeight;
    } else {
        dmMsgContainerElem.scrollTop = msgList.length * 72;
    }
}

function makeDmMsgItem(item) {
    //로그인 한 클라이언트와 타 클라이언트를 분류하기 위함
    const div = document.createElement('div');
    div.className = 'col-6';

    const inClassName = globalIuser == item.iuser ? 'alert-secondary' : 'alert-warning';
    div.innerHTML = `
        <div class='alert ${inClassName}'>
            <b>${item.msg}</b>
        </div>
    `;
    return div;
}


