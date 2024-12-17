let report_form_processing=false;

function sendReportToSv(event){
    event.preventDefault();
    if(!report_form_processing){
        report_form_processing=true;
        const form=event.target;
        const jsdata = new FormData(form);

        const form_id=form.closest(".reply").id;
        console.log(form.closest(".reply"));
        const senddata={
            id:null,
            text:jsdata.get('reporttext'),
            id_reply:parseInt(form_id.substring(4)),
            status:"WAIT"
        }
        document.getElementById('msginfo').setAttribute('style', 'display:block');
        document.getElementById('msginfo').textContent = "Отправка репорта...";
        fetch("/addReport",{
                method: 'post',
                headers: {'Content-Type': 'application/json'},
                body:JSON.stringify(senddata)
            }).then(response => {
            if (!response.ok) {
                console.log('error');document.getElementById('msginfo').textContent = "Ошибка отправки";
                setTimeout(() =>
                    document.getElementById('msginfo').setAttribute('style','display:none'), 7000
                );
            }else{
                document.getElementById('msginfo').textContent = "Репорт отправлен";
                setTimeout(() =>
                    document.getElementById('msginfo').setAttribute('style','display:none'), 3000
                );
            }
        })
            .catch(error => {
                console.error('Ошибка:', error);
            });
        report_form_processing=false;
    }
}

function addFormListener(forms){
    forms.forEach(form =>{
        form.addEventListener("submit", sendReportToSv);
    });
}

document.addEventListener("DOMContentLoaded",function (){
    const report_forms=document.querySelectorAll('.report-form');
    addFormListener(report_forms);
});