const user_form_element=document.getElementById('user_form');
const bord_form_element=document.getElementById('bord_form');
const ban_form_element=document.getElementById('ban_form');
const report_form_element=document.getElementById('report_form');
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
user_form_element.addEventListener('submit',addUser);
bord_form_element.addEventListener('submit',addBord);
ban_form_element.addEventListener('submit',banUser);
report_form_element.addEventListener('submit',updReport);

function ReportMenuDisplaySwitch(){
    if(document.getElementById('report_admin_menu').style['display']==='none'){
        document.getElementById('report_admin_menu').style['display']='block';
    }else{
        document.getElementById('report_admin_menu').style['display']='none'
    }
}

function BanFormDisplaySwitch(){
    if(document.getElementById('formban').style['display']==='none'){
        document.getElementById('formban').style['display']='block';
        if(document.getElementById('formuser').style['display']!=='none'){
            document.getElementById('formuser').style['display']='none';
        }
        if(document.getElementById('formbord').style['display']!=='none'){
            document.getElementById('formbord').style['display']='none';
        }
    }else{
        document.getElementById('formban').style['display']='none'
    }
}

function UserFormDisplaySwitch(){
    if(document.getElementById('formuser').style['display'] === 'none'){
        if(document.getElementById('formbord').style['display']!=='none'){
            document.getElementById('formbord').style['display']='none';
        }
        if(document.getElementById('formban').style['display']!=='none') {
            document.getElementById('formban').style['display'] = 'none';
        }
        document.getElementById('formuser').style['display']='block';
    }
    else{
        document.getElementById('formuser').style['display']='none';
    }
}

function BordFormDisplaySwitch(){
    if(document.getElementById('formbord').style['display']==='none'){
        if(document.getElementById('formuser').style['display']!=='none'){
            document.getElementById('formuser').style['display']='none';
        }
        if(document.getElementById('formban').style['display']!=='none') {
            document.getElementById('formban').style['display'] = 'none';
        }
        document.getElementById('formbord').style['display']='block';
    }
    else{
        document.getElementById('formbord').style['display']='none';
    }
}

function getReply(){
    const id=document.getElementById('get-reply-id').value;
    let str_req='¯\_(ツ)_/¯';
    if(id!==''){
        str_req='¯\_(ツ)_/¯';
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
        str_req='¯\_(ツ)_/¯';
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
        fetch('¯\_(ツ)_/¯', {
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
    fetch('¯\_(ツ)_/¯',{
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

function updReport(event){
    event.preventDefault();
    const jsdata=new FormData(document.getElementById('report_form'));
        fetch('¯\_(ツ)_/¯'),{
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
    if(id!=='' && id!=null){
        fetch("¯\_(ツ)_/¯",{
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
        fetch("¯\_(ツ)_/¯",{
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
    /*fetch('¯\_(ツ)_/¯',{
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
        fetch("¯\_(ツ)_/¯",{
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
        fetch("¯\_(ツ)_/¯",{
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
                document.getElementById('getter_info').innerHTML ='pinned: \n'+ '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
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

function Logout(){
    fetch("¯\_(ツ)_/¯",{
        method: 'post',
        headers:{
            [csrfHeader]:csrfToken,
        },
    }).then(response =>{
        if(!response.ok){
            document.getElementById('getter_info').innerHTML="Error was occurred";
            return;
        }
    });
}
