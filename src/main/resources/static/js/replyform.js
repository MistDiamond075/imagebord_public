let reply_form=document.getElementById('reply_form');
let reply_form_processing=false;
reply_form.addEventListener('submit', sendReplyTosv);

function addTextFormatStrong(){
document.getElementById('textzone').value+="[strong][/strong]";
}

function addTextFormatCursive(){
    document.getElementById('textzone').value+="[cursive][/cursive]";
}

function addTextFormatSpoiler(){
    document.getElementById('textzone').value+="[spoiler][/spoiler]";
}

function addRecieverToForm(num){
    let str=num;
    const val=document.getElementById('reply-form-receiver').value.split(',');
    if(val!=null && val[0]!==''){
        str=','+str;
    }
    console.log(val.length)
    if(val.includes(num.toString())){
        str='';
    }
    document.getElementById('reply-form-receiver').value+=str;
}

function removeReceiverFromForm(){
    document.getElementById('reply-form-receiver').value=null;
}

function sendReplyTosv(event){
    event.preventDefault();
    if(!reply_form_processing) {
        reply_form_processing=true;
        const form_data = new FormData();
        const jsdata = new FormData(document.getElementById('reply_form'));
        const isOP = document.getElementById('reply-form-op').checked;
        let attached_files = jsdata.getAll('formfile');
        if (attached_files[0].size <= 0 && attached_files[0].name === "") {
            attached_files = null;
        }
        let rcv = null;
        if (jsdata.get('receiver') !== null && jsdata.get('receiver') !== '') {
           rcv=jsdata.get('receiver').split(',');
           rcv=JSON.stringify(rcv);
        }
        const replyentitydata = {
            id: null,
            thread_id: null,
            text: jsdata.get('text'),
            date: getTime().toString(),
            imagepath: null,
            ipreplying: "",
            receiver: rcv,
            status: "sending",
        }
        document.getElementById('msginfo').setAttribute('style', 'display:block');
        document.getElementById('msginfo').textContent = "Отправка сообщения...";
        if (attached_files != null) {
            let vid_cnt=0;
            let pic_cnt=0;
            for (let i = 0; i < attached_files.length; i++) {
                if (pic_cnt+vid_cnt<4) {
                    console.log(i);
                    if(attached_files[i].type.indexOf("video")!==-1 && vid_cnt<2){
                        vid_cnt++;
                        form_data.append('files', attached_files[i]);
                    }
                    else if(attached_files[i].type.indexOf("image")!==-1){
                        pic_cnt++;
                        form_data.append('files', attached_files[i]);
                    }
                }
            }
        } else {
            form_data.append('files', null);
        }
        console.log(form_data.getAll('files'));
        form_data.append('replyentitydata', new Blob([JSON.stringify(replyentitydata)], {type: 'application/json'}));
        form_data.append('isOP', isOP);
        fetch("/addReply/" + jsdata.get('thread_id'), {
            method: 'post',
            body: form_data
        }).then(response => {
            if (!response.ok) {
                document.getElementById('msginfo').textContent = "Ошибка отправки";
                setTimeout(() =>
                    document.getElementById('msginfo').setAttribute('style','display:none'), 7000
                );
                reply_form_processing=false;
                throw new Error('Network response was not ok');
            }
            return response.json();
        }).then(data => {
            console.log('Response Body:', data);
            document.getElementById('msginfo').textContent = "Сообщение отправлено";
            setTimeout(() =>
                document.getElementById('msginfo').setAttribute('style','display:none'), 3000
            );
            //getMsgStatus(data.id);
            reply_form_processing=false;
        });
        console.log('reply send');
    }
}
