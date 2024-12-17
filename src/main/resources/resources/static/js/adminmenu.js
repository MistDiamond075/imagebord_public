const user_form_element=document.getElementById('user_form');
const bord_form_element=document.getElementById('bord_form');
const ban_form_element=document.getElementById('ban_form');
const report_form_element=document.getElementById('report_form');
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
let anyformactive=false;
user_form_element.addEventListener('submit',addUser);
bord_form_element.addEventListener('submit',addBord);
ban_form_element.addEventListener('submit',banUser);
report_form_element.addEventListener('submit',updReport);

function FormsMenuDisplaySwitch(number){
    if(anyformactive){
        if(document.getElementById('report_admin_menu').style['display']==='block' && number!=='report'){
            document.getElementById('report_admin_menu').style['display']='none';
        }
        if(document.getElementById('formban').style['display']==='block' && number!=='ban') {
            document.getElementById('formban').style['display']='none';
        }
        if(document.getElementById('formuser').style['display'] === 'block' && number!=='user'){
            document.getElementById('formuser').style['display']='none';
        }
        if(document.getElementById('formbord').style['display']==='block' && number!=='bord'){
            document.getElementById('formbord').style['display']='none';
        }
        if(document.getElementById('formaft').style['display']==='block' && number!=='aft'){
            document.getElementById('formaft').style['display']='none';
        }
    }
    switch(number){
        case 'report':{
            if(document.getElementById('report_admin_menu').style['display']==='none'){
                document.getElementById('report_admin_menu').style['display']='block';
                anyformactive=true;
            }else{
                document.getElementById('report_admin_menu').style['display']='none';
                anyformactive=false;
            }
        break;}
        case 'ban':{
            console.log(document.getElementById('formban').style['display']);
            if(document.getElementById('formban').style['display']==='none'){
                document.getElementById('formban').style['display']='block';
                anyformactive=true;
            }else{
                document.getElementById('formban').style['display']='none';
                anyformactive=false;
            }
        break;}
        case 'user':{
            if(document.getElementById('formuser').style['display'] === 'none'){
                document.getElementById('formuser').style['display']='block';
                anyformactive=true;
            }else{
                document.getElementById('formuser').style['display']='none';
                anyformactive=false;
            }
        break;}
        case 'bord':{
            if(document.getElementById('formbord').style['display']==='none'){
                document.getElementById('formbord').style['display']='block';
                anyformactive=true;
            }
            else{
                document.getElementById('formbord').style['display']='none';
                anyformactive=false;
            }
        break;}
        case 'aft':{
            if(document.getElementById('formaft').style['display']==='none'){
                document.getElementById('formaft').style['display']='block';
                anyformactive=true;
            }
            else{
                document.getElementById('formaft').style['display']='none';
                anyformactive=false;
            }
        break;}
    }
}

function getReply(){
    const id=document.getElementById('get-reply-id').value;
    let str_req='¯\_(ツ)_/¯';
    if(id!==''){
        str_req='¯\_(ツ)_/¯/'+id;
    }
    fetch(str_req,{
        method: 'get'
    }).then(response => {
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred. Reply not found";
            return null;
        }
        return response.json();
    }).then(data => {
        if(data!=null) {
            document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function getThread(){
    const id=document.getElementById('get-thread-id').value;
    let str_req='¯\_(ツ)_/¯';
    if(id!=='' && id!=null){
        str_req='¯\_(ツ)_/¯/'+id;
    }
    fetch(str_req,{
        method: 'get'
    }).then(response => {
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred. Thread not found";
            return null;
        }
        return response.json();
    }).then(data => {
        if(data!=null) {
            document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function getRepliesByThreadId(){
    const id=document.getElementById('get-replies-by-thread').value;
    if(id!=='' && id!=null) {
        fetch('¯\_(ツ)_/¯/' + id, {
            method: 'get'
        }).then(response => {
            if (!response.ok) {
                document.getElementById('getter_info').innerHTML = "Error was occurred. Thread not found";
                return null;
            }
            return response.json();
        }).then(data => {
            if (data != null) {
                document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                return data;
            }
        });
    }
}

function getBanlist(){
    fetch('¯\_(ツ)_/¯',{
        method: 'get'
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred. Banlist not found";
            return null;
        }
        return response.json();
    }).then(data => {
        if (data != null) {
            document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function getReports(status){
    if(status===undefined){status='';}
    fetch('¯\_(ツ)_/¯/'+status,{
        method: 'get'
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred. Reports not found";
            return null;
        }
        return response.json();
    }).then(data => {
        if (data != null) {
            document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function getBords(){
    const id=document.getElementById('get-bord-by-id').value;
    let str_req='/¯\_(ツ)_/¯';
    if(id!=='' && id!=null){
        str_req='/getBord/'+id;
    }
    fetch(str_req,{
        method: 'get'
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred. Bords not found";
            return null;
        }
        return response.json();
    }).then(data => {
        if (data != null) {
            document.getElementById('getter_info').innerHTML = '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function updReport(event){
    event.preventDefault();
    const jsdata=new FormData(document.getElementById('report_form'));
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        fetch("¯\_(ツ)_/¯/"+jsdata.get('reportid')+'/'+jsdata.get('reportstatus'),{
            method:'PATCH',
            headers:{
                [csrfHeader]:csrfToken,
            },
        }).then(response => {
            if (!response.ok) {
                document.getElementById('getter_info').innerHTML = "Error was occurred. Report not found";
                return null;
            }
            return response.json();
        }).then(data => {
            if (data != null) {
                document.getElementById('getter_info').innerHTML ='report updated: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                return data;
            }
        });
}

function delThread(){
    const id=document.getElementById('del-thread-id').value;
    console.log("deleting [" +id +"]");
    if(id!=='' && id!=null){
        fetch("¯\_(ツ)_/¯/"+id,{
            method: 'delete',
            headers:{
                [csrfHeader]:csrfToken,
            },
        }).then(response =>{
            if(!response.ok){
                document.getElementById('getter_info').innerHTML="Error was occurred. Thread will not be deleted";
                return;
            }
            return response.json();
        }).then(data => {
            if(data!=null) {
                document.getElementById('getter_info').innerHTML ='deleted:\n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                return data;
            }
        });
    }
}

function delReply(){
    const id=document.getElementById('del-reply-id').value;
    if(id!=='' && id!=null){
        fetch("¯\_(ツ)_/¯/"+id,{
            method: 'delete',
            headers:{
                [csrfHeader]:csrfToken,
            },
        }).then(response =>{
            if(!response.ok){
                document.getElementById('getter_info').innerHTML="Error was occurred. Reply will not be deleted";
                return;
            }
            return response.json();
        }).then(data => {
            if(data!=null) {
                document.getElementById('getter_info').innerHTML ='deleted: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
                return data;
            }
        });
    }
}

function banUser(event){
    event.preventDefault();
    const jsdata=new FormData(document.getElementById('ban_form'));
    const senddata={
        id:null,
        ip:jsdata.get('ip'),
        cause:jsdata.get('cause'),
        period:jsdata.get('period')
    }
    fetch("¯\_(ツ)_/¯",{
        method:'post',
        headers:{
            'Content-Type': 'application/json',
            [csrfHeader]:csrfToken,
        },
        body:JSON.stringify(senddata)
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred. User will not be banned";
            return null;
        }
        return response.json();
    }).then(data => {
        if (data != null) {
            document.getElementById('getter_info').innerHTML ='banned:'+'\n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            return data;
        }
    });
}

function addUser(event){
    event.preventDefault();
    const jsdata=new FormData(document.getElementById('user_form'));
    const senddata={
        id:null,
        username:jsdata.get('username'),
        password:jsdata.get('password'),
        role:jsdata.get('role')
    }
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    fetch('¯\_(ツ)_/¯',{
        method: 'post',
        headers:{
            'Content-Type': 'application/json',
            [csrfHeader]:csrfToken,
        },
        body:JSON.stringify(senddata)
    }).then(response =>{
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred. User will not be added";
            return;
        }
        return response.json();
    }).then(data => {
        document.getElementById('getter_info').innerHTML ='new user: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
        return data;
    });
}

function addBord(event){
    event.preventDefault();
    const jsdata=new FormData(document.getElementById('bord_form'));
    const senddata={
        id:null,
        name:jsdata.get('bordshortname'),
        postcount:0,
        fullname:jsdata.get('bordfullname'),
        activethreads:0,
        description:jsdata.get('borddesc')
    }
    //console.log(JSON.stringify(senddata));
    /*fetch('/¯\_(ツ)_/¯',{
        method: 'post',
        headers:{'Content-Type': 'application/json'},
        body:JSON.stringify(senddata)
    }).then(response =>{
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred. Bord will not be added";
            return;
        }
        return response.json();
    }).then(data => {
        document.getElementById('getter_info').innerHTML ='new bord: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
    });*/
}

function pinThread(){
    const id=document.getElementById('pin-thread-id').value;
    if(id!=='' && id!=null){
        fetch("¯\_(ツ)_/¯/"+id+"/true",{
            method: 'PATCH',
            headers:{
                [csrfHeader]:csrfToken,
            },
        }).then(response =>{
            if(!response.ok){
                document.getElementById('getter_info').innerHTML="Error was occurred. Thread will not be pinned";
                return;
            }
            return response.json();
        }).then(data => {
            if(data!=null) {
                document.getElementById('getter_info').innerHTML ='pinned: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            }
        });
    }
}

function lockThread(){
    const id=document.getElementById('lock-thread-id').value;
    if(id!=='' && id!=null){
        fetch("¯\_(ツ)_/¯/"+id+"/true",{
            method: 'PATCH',
            headers:{
                [csrfHeader]:csrfToken,
            },
        }).then(response =>{
            if(!response.ok){
                document.getElementById('getter_info').innerHTML="Error was occurred. Thread will not be locked";
                return;
            }
            return response.json();
        }).then(data => {
            if(data!=null) {
                document.getElementById('getter_info').innerHTML ='locked: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            }
        });
    }
}

function getAuditJournal(){
    fetch("¯\_(ツ)_/¯",{
        method: 'get'
    }).then(response =>{
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred";
            return;
        }
        return response.text();
    }).then(data => {
        if(data!=null) {
            document.getElementById('getter_info').innerHTML ='<pre>' +data+ '</pre>';
        }
    });
}

function addTextAutoFormatList(){
    let bordname=document.getElementById('bordaftadd').value;
    let sendstr=document.getElementById('aft_txtzone').value;
    if(bordname!=='') {
        const sendarray = sendstr.split('\n');
        fetch("¯\_(ツ)_/¯" +bordname, {
            method: 'post',
            headers: {"Content-Type": "application/json",[csrfHeader]:csrfToken},
            body: JSON.stringify(sendarray)
        }).then(response => {
            if (!response.ok) {
                document.getElementById('getter_info').innerHTML = "Error was occurred";
                return;
            }
            return response.text();
        }).then(data => {
            if (data != null) {
                document.getElementById('getter_info').innerHTML = '<pre>' + data + '</pre>';
            }
        });
    }
}

function getTextAutoFormatList(){
    let bordname=document.getElementById('bordaft').value;
    console.log(bordname);
    if(bordname!=='') {
        fetch("¯\_(ツ)_/¯" +bordname, {
            method: 'get'
        }).then(response => {
            if (!response.ok) {
                document.getElementById('getter_info').innerHTML = "Error was occurred";
                return;
            }
            return response.text();
        }).then(data => {
            if (data != null) {
                document.getElementById('getter_info').innerHTML = '<pre>'+'Включена: ' + data + '</pre>';
            }
        });
        fetch("¯\_(ツ)_/¯" +bordname, {
            method: 'get'
        }).then(response => {
            if (!response.ok) {
                document.getElementById('getter_info').innerHTML = "Error was occurred";
                return;
            }
            return response.text();
        }).then(data => {
            if (data != null) {
                document.getElementById('getter_info').innerHTML+= '<pre>' + data + '</pre>';
            }
        });
    }
}

function getTextAutoFormatListForForm(){
    let bordname=document.getElementById('bordaftadd').value;
    fetch("¯\_(ツ)_/¯" +bordname, {
        method: 'get'
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred";
            return;
        }
        return response.text();
    }).then(data => {
        if (data != null) {
            let parsedarray=JSON.parse(data);
            parsedarray=parsedarray.join('\n');
            document.getElementById('aft_txtzone').value= parsedarray ;
        }
    });
}

function changeTextAutoFormatState(){
    let bordname=document.getElementById('bordaftupdstate').value;
    let aftstate=document.getElementById('stateaft').value;
    fetch("¯\_(ツ)_/¯" +bordname+"?"+new URLSearchParams({isAutoformat:aftstate}), {
        method: 'post',
        headers:{
            [csrfHeader]:csrfToken,
        },
    }).then(response => {
        if (!response.ok) {
            document.getElementById('getter_info').innerHTML = "Error was occurred";
            return;
        }
        return response.text();
    }).then(data => {
        if (data != null) {
            document.getElementById('getter_info').innerHTML= '<pre>' + data + '</pre>';
        }
    });
}

function Logout(){
    fetch("logout",{
        method: 'post',
        headers:{
            [csrfHeader]:csrfToken,
        },
    }).then(response =>{
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred";
            return;
        }else{
            document.getElementById('getter_info').innerHTML="Logged out successfully. You can close this page now";
        }
    });
}