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

function addReplyToPage(jsdata){
    let newelement=document.createElement('div');
    newelement.setAttribute('id','rep_'+jsdata.id);
    newelement.setAttribute('class','reply');
    document.getElementsByClassName('threads')[0].appendChild(newelement);
    newelement=document.createElement('div');
    newelement.setAttribute('id','recv_'+jsdata.number);
    console .log(newelement);
    document.getElementById('rep_'+jsdata.id).appendChild(newelement);
    newelement=document.createElement('div');
    newelement.setAttribute('class','name-date-time');
    document.getElementById('recv_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('span');
    newelement.innerText=" Аноним ";
    document.getElementById('recv_'+jsdata.number).getElementsByClassName('name-date-time')[0].appendChild(newelement);
    newelement=document.createElement('span');
    newelement.innerText=jsdata.date;
    document.getElementById('recv_'+jsdata.number).getElementsByClassName('name-date-time')[0].appendChild(newelement);
    newelement=document.createElement('span');
    newelement.innerHTML='<a onclick=addRecieverToForm(' + jsdata.number + ') style="cursor: pointer;"> №'+jsdata.number+'</a>';
    document.getElementById('recv_'+jsdata.number).getElementsByClassName('name-date-time')[0].appendChild(newelement);
    if(jsdata.op){
        newelement=document.createElement('span');
        newelement.setAttribute('class','OP-lable');
        newelement.innerText='!TC';
        document.getElementById('recv_'+jsdata.number).getElementsByClassName('name-date-time')[0].appendChild(newelement);
    }
    newelement=document.createElement('span');
    newelement.innerHTML= '<button type="button" name="replyprop" class="replyprop-btn" onclick=reportFormCssChange('+jsdata.number+')>'+'︙'+'</button>';
    document.getElementById('recv_'+jsdata.number).getElementsByClassName('name-date-time')[0].appendChild(newelement);
    newelement=document.createElement('div');
    newelement.setAttribute('class','attached_file');
    document.getElementById('recv_'+jsdata.number).appendChild(newelement);
    console.log(jsdata.img_paths);
    if(jsdata.img_paths!=null){
        let i=0;
        for (const item of jsdata.img_paths) {
            console.log(item.toString().indexOf("thumb"));
            if(item.toString().indexOf("thumb")>=0){
                newelement=document.createElement('img');
                newelement.style.display='block';
                newelement.setAttribute('class','attached-file-thumb lazy');
                newelement.setAttribute('src',item.toString());
                newelement.setAttribute('id',jsdata.id+'_'+i+'_img_thumb');
                i++;
                newelement.setAttribute('onclick','createContentwindow(&quot;&quot;,&quot;image&quot;)');
                document.getElementById('recv_'+jsdata.number).getElementsByClassName('attached_file')[0].appendChild(newelement);
            }else if(item.toString().indexOf("vid")>=0){
                newelement=document.createElement('video');
                newelement.setAttribute('class','video-file lazy');
                newelement.setAttribute('src',item.toString());
                newelement.setAttribute('id',jsdata.id+'_'+i+'_vid');
                i++;
                newelement.setAttribute('onclick','createContentwindow(&quot;&quot;,&quot;video&quot;)');
                document.getElementById('recv_'+jsdata.number).getElementsByClassName('attached_file')[0].appendChild(newelement);
            }
        }
    }
    if(jsdata.receiver_list!=null){
        newelement = document.createElement('div');
        newelement.setAttribute('class','reply-to-all')
        document.getElementById('recv_'+jsdata.number).appendChild(newelement);
        for(const item of jsdata.receiver_list) {
            newelement = document.createElement('span');
            newelement.setAttribute('class','reply-to');
            newelement.innerHTML ='<a onclick=addRecieverToForm('+item.toString()+');scrollToReply('+item.toString()+') style="cursor: pointer;" >>>'+item.toString()+'</a><br>';
            document.getElementById('recv_'+jsdata.number).getElementsByClassName('reply-to-all')[0].appendChild(newelement);
        }
    }
    newelement=document.createElement('span');
    newelement.setAttribute('class','reply-text');
    newelement.innerHTML=jsdata.text;
    document.getElementById('recv_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('div');
    newelement.setAttribute('class','report-form');
    newelement.setAttribute('id','report_form_form_'+jsdata.number);
    newelement.style.display='none';
    document.getElementById('recv_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('span');
    newelement.style.fontStyle='italic';
    newelement.style.color='#df9898';
    newelement.innerText='Репорт: ';
    document.getElementById('report_form_form_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('form');
    newelement.setAttribute('id','report_form_'+jsdata.number);
    document.getElementById('report_form_form_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('input');
    newelement.setAttribute('name','reporttext');
    newelement.setAttribute('type','text');
    newelement.setAttribute('id','report_text_field');
    newelement.setAttribute('placeholder','Текст жалобы...');
    newelement.setAttribute('required','');
    document.getElementById('report_form_'+jsdata.number).appendChild(newelement);
    newelement=document.createElement('button');
    newelement.setAttribute('type','submit');
    newelement.innerText='Отправить';
    document.getElementById('report_form_'+jsdata.number).appendChild(newelement);
}

function sendReplyTosv(event){
    event.preventDefault();
    if(!reply_form_processing) {
        reply_form_processing=true;
        const form_data = new FormData();
        const jsdata = new FormData(document.getElementById('reply_form'));
        const isOP = document.getElementById('reply-form-op').checked;
        const isAdmin = document.getElementById('reply-form-privileged')!=null || undefined ? document.getElementById('reply-form-privileged').checked : "null";
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
            postername: isAdmin,
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
                    document.getElementById('msginfo').setAttribute('style', 'display:none'), 7000
                );
                reply_form_processing = false;
                throw new Error('Network response was not ok');
            }
            return response.text().then(text => text ? JSON.parse(text) : null);
        }).then(data => {
            if(data==null){
                document.getElementById('msginfo').textContent = "Ошибка отправки";
                setTimeout(() =>
                    document.getElementById('msginfo').setAttribute('style', 'display:none'), 7000
                );
                reply_form_processing = false;
                throw new Error('Response data was null');
            }
            console.log('Response Body:', data);
            addReplyToPage(data);
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
