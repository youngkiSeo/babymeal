(function() {
    const $frm = document.querySelector('#frm');
    /*
    $frm.addEventListener('submit', e => {
        e.preventDefault();
    });
    */
    $frm.btn_submit.addEventListener('click', e => {
        e.preventDefault();

        const param = {
            uid: $frm.uid.value,
            upw: $frm.upw.value
        }

        myFetch.post('/api/v1/auth/sign-in', function(res) {
            console.log(res);
            if(res.accessToken) {
                window.localStorage.setItem("access_token", res.accessToken);
                location.href = '/feed';
            }
        }, param);
    })
})();