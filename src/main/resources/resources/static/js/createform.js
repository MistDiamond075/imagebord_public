let create_form=document.getElementById('create-thread-form');
let create_form_processing=false;
create_form.addEventListener('submit', SendNewThreadTosv);

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
    document.getElementById('reply-form-receiver').value=num;
}

function SendNewThreadTosv(event){
   event.preventDefault();
if(!create_form_processing){
create_form_processing=true;
    const form_data=new FormData();
    const jsdata=new FormData(document.getElementById('create-thread-form'));
    let attached_files=jsdata.getAll('formfile');
    if(attached_files[0].size<=0 && attached_files[0].name===""){
        attached_files=null;
    }
    const senddata={
        id:null,
        bord_id:null,
        fposttext: jsdata.get('text'),
        date:getTime().toString(),
        ipcreator: null,
        pinned:false,
        locked:false,
        postcount:0,
        cap:500
    }
    if(attached_files!=null) {
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
    }
    else{
        form_data.append('files', null);
    }
    form_data.append('threadentitydata',new Blob([JSON.stringify(senddata)],{type: 'application/json'}));
    document.getElementById('msginfo').setAttribute('style','display:block');
    document.getElementById('msginfo').textContent="Отправка сообщения...";
    fetch("/addThread/"+jsdata.get('bord_id'), {
        method: 'post',
        body: form_data
    }).then(response => {
        if(!response.ok){
            document.getElementById('msginfo').textContent = "Ошибка отправки";
            setTimeout(() =>
                document.getElementById('msginfo').setAttribute('style','display:none'), 7000
            );
            create_form_processing=false;
            throw new Error('Network response was not ok');
        }
       return response.json()
    })/*.then(data =>{
        console.log('Response Body:', data);
        //getMsgStatus(data.id);
    })*/.then(response => {console.log(response);create_form_processing=false;location.href=response.bordid.name+"/thread/"+response.id;});  //response.bord_id.name+"/"+
    //console.log(response.json());
}
}
