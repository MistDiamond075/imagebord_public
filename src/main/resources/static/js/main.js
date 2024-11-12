let is_thread_form_active=false;
let is_overlay_bords_active=false;
let imageObserver;
let contentwindow_h_max;
let contentwindow_w_max;
const form_length_max=1000;

/*css changing part*/
function OverlayBordsCssChange(){
   if(!is_overlay_bords_active){
      if(document.getElementById('overlay_settings').style['display']==='block') {
         document.getElementById('overlay_settings').style['display']='none';
      }
      document.getElementsByClassName("overlaydropdownbords")[0].style['display']="block";is_overlay_bords_active=true;}
   else{document.getElementsByClassName("overlaydropdownbords")[0].style['display']="none";is_overlay_bords_active=false;}
}

function OverlaySettingsCssChange(){
   if(document.getElementById('overlay_settings').style['display']==='none'){
      if( document.getElementsByClassName("overlaydropdownbords")[0].style['display']==="block"){
         document.getElementsByClassName("overlaydropdownbords")[0].style['display']="none"
      }
      document.getElementById('overlay_settings').style['display']='block'
   }else{
      document.getElementById('overlay_settings').style['display']='none';
   }
}

function createThreadButtonCss (){
   if(!is_thread_form_active){
   document.getElementsByClassName("create-thread-form")[0].style['display']="block";is_thread_form_active=true;}
   else{document.getElementsByClassName("create-thread-form")[0].style['display']="none";is_thread_form_active=false;}
}

function imgSwitchThumb(id){
   console.log(id);
   if(document.getElementById(id+"_img_thumb").style["display"]==="block"){
      document.getElementById(id+"_img_thumb").style["display"]="none";
      document.getElementById(id+"_img").style["display"]="block";
   }else{
      document.getElementById(id+"_img_thumb").style["display"]="block";
      document.getElementById(id+"_img").style["display"]="none";
   }
}

function vidSwitchSize(id){
   if(document.getElementById(id+"_vid").style["width"]==="200px"){
      document.getElementById(id+"_vid").style["width"]="500px";
   }else{
      document.getElementById(id+"_vid").style["width"]="200px";
   }
}

function reportFormCssChange(id){
   if(document.getElementById('report_form_form_'+id).style["display"]==="none"){
      document.getElementById('report_form_form_'+id).style["display"]="block";
   }else{
      document.getElementById('report_form_form_'+id).style["display"]="none";
   }
}

function formCharCount(){
   const textarea = document.getElementById('textzone');
   const charCount = textarea.value.length;
   document.getElementById('form_char_count').textContent = `${charCount}/`+form_length_max;
}

/*misc functions*/
function getTime(){
   const date=new Date;
   return DateTimeToFormat(date.getDate())+"."+DateTimeToFormat(date.getMonth()+1)+"."
       +date.getFullYear()+" "+DateTimeToFormat(date.getHours())+":"+DateTimeToFormat(date.getMinutes())+":"+DateTimeToFormat(date.getSeconds());
}

function DateTimeToFormat(dateortime){
   if(dateortime.toString().length===1){
      return "0"+dateortime;
   }else{
      return dateortime;
   }
}

function getMsgStatus(id) {
   if(id!=null) {
      const eventSource = new EventSource('/' + id + '/ReplyStatus');
      eventSource.onmessage = function (event) {
         if (event.data === 'Сообщение отправлено') {
            eventSource.close();
            setTimeout(() =>
                document.getElementById('msginfo').setAttribute('style','display:none'), 3000
            );
         }
         else {
            console.log(event.data);
            document.getElementById('msginfo').textContent=event.data.toString();
         }
      };
      eventSource.onerror = function (event) {
         console.log('Error: ', event);
         eventSource.close();
         document.getElementById('msginfo').setAttribute('style','display:none');
      };
   }
}

function redirectFromBordPageByReply(th_id,rep_id){
   //const thread_id=document.getElementById('recv_'+th_id).parentElement.parentElement.id.match(/\d+/);
   let current_url = window.location.pathname;
   if (current_url.endsWith("/")) {
      current_url = current_url.slice(0, -1);
   }
   location.href=current_url+'/thread/'+th_id;
   if(rep_id!==null) {
      scrollToReply(rep_id);
   }
 //  addRecieverToForm(id);
}

function scrollToReply(id) {
   const element = document.getElementById("recv_"+id);
   const rect = element.getBoundingClientRect();
   window.scrollTo({
      top: window.scrollY + rect.top,
   });
}

function createContentwindow(srcpath,srctype){
   const win=document.getElementById('content_window');
   const winfile=document.getElementById('content_window_file');
   let file_h;
   let file_w;
   winfile.innerHTML='';
   switch(srctype){
      case 'image':{
         const img=document.createElement('img');
         img.src=srcpath;
         winfile.appendChild(img);
         img.onload=function (){
            file_h=img.naturalHeight;
            file_w=img.naturalWidth;
            if(file_h>400 || file_w>400) {
               file_h = img.naturalHeight / 3;
               file_w=img.naturalWidth /3;
            }
            contentwindow_h_max=file_h;
            contentwindow_w_max=file_w;
            win.style.height=file_h+'px';
            win.style.width=file_w+'px';
         }
         break;}
      case 'video': {
         const vid = document.createElement('video');
         vid.src = srcpath;
         vid.controls = true;
         winfile.appendChild(vid);
         vid.onloadeddata=function (){
         file_h = vid.videoHeight;
         file_w = vid.videoWidth;
            console.log(file_h);
         if (file_h > 400 || file_w > 400) {
            file_h = vid.videoHeight / 3;
            file_w = vid.videoWidth / 3;
         }
         contentwindow_h_max = file_h;
         contentwindow_w_max = file_w;
         win.style.height = file_h + 'px';
         win.style.width = file_w + 'px';
      }
         break;}
   }
   win.style.display='block';
   win.style.position='fixed';
   win.style.top='calc(50vh-50%)';
   win.style.right='calc(50vh-50%)';
   win.style.left='calc(50vh-50%)';
   win.style.bottom='calc(50vh-50%)';
   addMovableProperty(win);
   const close_button=document.getElementById('close_window_btn');
   close_button.ontouchstart=closeWindow;
   function closeWindow(e){
      e.preventDefault();
      win.style.display='none';
   }
}

function closeContentwindow(){
   const win=document.getElementById('content_window');
   win.style.display='none';
}

function addMovableProperty(element){
   let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
   element.onmousedown = dragMouseDown;
   element.ontouchstart=dragMouseDown;

   function dragMouseDown(e) {
      e.preventDefault();
      console.log(e.type);
      if(e.type==="touchstart"){
         pos3 = e.touches[0].clientX;
         pos4 = e.touches[0].clientY;
         document.ontouchend = closeDragElement;
         document.ontouchmove = elementDrag;
      }
      else {
         pos3 = e.clientX;
         pos4 = e.clientY;
         document.onmouseup = closeDragElement;
         document.onmousemove = elementDrag;
      }
   }

   function elementDrag(e) {
      e.preventDefault();
      let client_x=null;
      let client_y=null;
      if(e.type==="touchmove") {
          client_x =e.touches[0].clientX;
          client_y =e.touches[0].clientY;
      }else{
         client_x =e.clientX;
         client_y =e.clientY;
      }
      console.log(client_x);
      pos1 = pos3 - client_x;
      pos2 = pos4 - client_y;
      pos3 = client_x;
      pos4 = client_y;
      element.style.top = (element.offsetTop - pos2) + "px";
      element.style.left = (element.offsetLeft - pos1) + "px";
   }

   function closeDragElement() {
      document.onmouseup = null;
      document.onmousemove = null;
      document.ontouchend = null;
      document.ontouchmove = null;
   }
}

function addContentwindowResizeEvent(){
   document.getElementById('content_window').addEventListener('wheel',function (event){
      event.preventDefault();
      const window_element=event.currentTarget;
      const resize_h=contentwindow_h_max/2;
      const resize_w=contentwindow_w_max/2;
      let resized_h=window_element.offsetHeight+(event.deltaY < 0 ? resize_h : -resize_h);
      let resized_w=window_element.offsetWidth+(event.deltaY < 0 ? resize_w : -resize_w);
      resized_h=Math.max(resized_h,contentwindow_h_max);
      resized_w=Math.max(resized_w,contentwindow_w_max);
      window_element.style.height=resized_h+'px';
      window_element.style.width=resized_w+'px';
      console.log(contentwindow_h_max);
   });
}

function addLazyLoadObserver(){
   imageObserver = new IntersectionObserver((entries, observer) => {
      entries.forEach(entry => {
         if (entry.isIntersecting) {
            const img = entry.target;
            img.src = img.dataset.src;
            img.classList.remove('lazy');
            observer.unobserve(img);
         }
      });
   });
}

function addLazyLoadForFile(posts){
   posts.forEach(image => {
      imageObserver.observe(image);
   });
}

function addLinkEventListener(replyLinks){
   replyLinks.forEach(link => {
      //console.log(link);
      let post_clonned = null;
      let ms_on_post = false;
      let ms_on_post_orig=false;

      const removeClonedPostIfNecessary = () => {
         if (!ms_on_post && !ms_on_post_orig && post_clonned) {
            post_clonned.remove();
            post_clonned = null;
         }
      };
      link.addEventListener('mouseenter', function() {
         ms_on_link = true;
         const post_id = link.textContent.replace('>>', '');
         const post_orig = document.getElementById('recv_' + post_id.match(/\d+/));
         if (post_orig && !post_clonned) {
            post_clonned = post_orig.cloneNode(true);
            post_clonned.classList.add('reply-hover');
            post_clonned.classList.add('reply');
            post_clonned.id = '';
            addLazyLoadForFile(post_clonned.querySelectorAll('img,video'));
            if(post_clonned.querySelectorAll('.reply-from a').length!==0) {
               addLinkEventListener(post_clonned.querySelectorAll('.reply-from a'));
            }
            link.closest('.reply').append(post_clonned);
            ms_on_post=true;
            link.closest('.reply').addEventListener('mouseleave', function() {
               ms_on_post_orig = false;
               removeClonedPostIfNecessary();
            });
            link.closest('.reply').addEventListener('mouseenter', function() {
               ms_on_post_orig = true;
            });
            post_clonned.addEventListener('mouseenter', function() {
               ms_on_post = true;
            });
            post_clonned.addEventListener('mouseleave', function() {
               ms_on_post = false;
               removeClonedPostIfNecessary();
            });
         }
      });
   });
}

document.addEventListener('DOMContentLoaded', function(){
   const replyLinks = document.querySelectorAll('.reply-from a');
   const lazyImages = document.querySelectorAll('.lazy');
   addLazyLoadObserver();
   addLinkEventListener(replyLinks);
   addLazyLoadForFile(lazyImages);
   addContentwindowResizeEvent();
}, false);

/** CLOSE CONTENTWINDOW ON CLICK ON DOCUMENT
document.addEventListener('click', function(event) {
   const contentWindow = document.getElementById('content_window');
   if (contentWindow.style.display === 'block' && !contentWindow.contains(event.target)) {
      closeContentWindow();
   }
});
**/