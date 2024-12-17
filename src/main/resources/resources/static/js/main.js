let is_thread_form_active=false;
let is_overlay_bords_active=false;
let imageObserver;
let contentwindow_h_max;
let contentwindow_w_max;
const form_length_max=10000;

const emoticon_list=[{key:":)",value:"Улыбка"},{key:":(",value:"Грусть"},{key:":p",value:"Показать язык"},{key:"^_^",value:"Радость"},{key:"-_-",value:"Флегматичный"},{key:":O",value:"Удивление"},
   {key:"о_О",value:"Удивление"},{key:":-{}",value:"Сильное удивление"},{key:"(͡° ͜ʖ ͡°)",value:"Сарказм"},{key:"o/",value:"Приветствие"},{key:"(v_v)",value:"Грусть"},
   {key:">_<",value:"Усталость"},{key:"(>x<!)",value:"Недовольство"},{key:"=__=",value:"Сонный"},{key:"(´･ω･`)",value:"Кот"},{key:"(´；ω；`)",value:"Очень грустно"},
   {key:"ヽ(´ー｀)ﾉ",value:"Спокойствие"},{key:"(￣ー￣)",value:"Ухмылка"},{key:"T_T",value:"Плачу"},{key:"(ツ)",value:"Улыбка"},{key:"(⌐■_■)",value:"Крутой"},
   {key:"(~‾⌣‾)~",value:"Танцую"},{key:"¯\\_(ツ)_/¯",value:"Не знаю"},{key:"(=ʘᆽʘ=)",value:"Кот"},{key:"(〃ー〃)",value:"Смущение"},{key:"( ´･_･`)",value:"Не понял"}];

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

function changeEmoticonMenuButtonCss(){

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

function changeAutoFormattedText(){
   function getRandomColor() {
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
         color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
   }
   function getBright(color){
      const rgb = color.slice(1).match(/.{2}/g).map(val => parseInt(val, 16) / 255);
      const [r, g, b] = rgb.map(val => {
         return val <= 0.03928 ? val / 12.92 : Math.pow((val + 0.055) / 1.055, 2.4);
      });
      return 0.2126 * r + 0.7152 * g + 0.0722 * b;
   }
   function getContrast(color1,color2){
      const cntrst1 = getBright(color1);
      const cntrst2 = getBright(color2);
      const brightest = Math.max(cntrst1, cntrst2);
      const darkest = Math.min(cntrst1, cntrst2);
      return (brightest + 0.05) / (darkest + 0.05);
   }
   function getMatchedColorForColor(color){
      let newcolor=getRandomColor();
      if(getContrast(color,newcolor)<4) {
         while (getContrast(color, newcolor) < 4) {
            newcolor = getRandomColor();
         }
      }
      return newcolor;
   }
   const textElements = document.querySelectorAll('.txt-replaced');
   textElements.forEach(textElement => {
      const bgcolor= getRandomColor();
      textElement.style.background = bgcolor;
      textElement.style.color = getMatchedColorForColor(bgcolor);
   });
}

function addTextEmoticon(id){
   document.getElementById('textzone').value+=emoticon_list[id].key;
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
      case 'image': {
         const img = document.createElement('img');
         img.src = srcpath;
         winfile.appendChild(img);
         img.onload = function () {
            file_h = img.naturalHeight;
            file_w = img.naturalWidth;
            const maxWidth = window.innerWidth;
            const maxHeight = window.innerHeight;
            const aspectRatio = file_w / file_h;
            if (file_h > maxHeight || file_w > maxWidth) {
               if (file_w > file_h) {
                  file_w = maxWidth;
                  file_h = maxWidth / aspectRatio;
               } else {
                  file_h = maxHeight;
                  file_w = maxHeight * aspectRatio;
               }
               if (file_h > maxHeight) {
                  file_h = maxHeight;
                  file_w = maxHeight * aspectRatio;
               }
               if (file_w > maxWidth) {
                  file_w = maxWidth;
                  file_h = maxWidth / aspectRatio;
               }
            }
            win.style.height = file_h + 'px';
            win.style.width = file_w + 'px';
            win.style.maxheight = 100 + 'vh';
            win.style.maxwidth = 100 + 'vh';
         }
         break;
      }
      case 'video': {
         const vid = document.createElement('video');
         vid.src = srcpath;
         vid.controls = true;
         winfile.appendChild(vid);
         console.log(vid.videoWidth);
         console.log(vid.videoHeight);
         console.log(window.innerWidth);
         console.log(window.innerHeight);
         vid.onloadeddata = function () {
            file_h = vid.videoHeight;
            file_w = vid.videoWidth;
            const maxWidth = window.innerWidth;
            const maxHeight = window.innerHeight;
            const aspectRatio = file_w / file_h;
            if (file_h > maxHeight || file_w > maxWidth) {
               if (file_w > file_h) {
                  file_w = maxWidth;
                  file_h = maxWidth / aspectRatio;
               } else {
                  file_h = maxHeight;
                  file_w = maxHeight * aspectRatio;
               }
               if (file_h > maxHeight) {
                  file_h = maxHeight;
                  file_w = maxHeight * aspectRatio;
               }
               if (file_w > maxWidth) {
                  file_w = maxWidth;
                  file_h = maxWidth / aspectRatio;
               }
            }
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
   const close_button = document.getElementById('close_window_btn');
   close_button.addEventListener('touchstart', closeWindow, { passive: false });
   close_button.addEventListener('click', closeWindow);

   function closeWindow(e){
      e.preventDefault();
      win.style.display='none';
   }
}

function closeContentwindow(){
   console.log("closed");
   const win=document.getElementById('content_window');
   win.style.display='none';
}


function addMovableProperty(element){
   let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
   element.onmousedown = dragMouseDown;
   element.getElementsByClassName('content-window-file')[0].ontouchstart=dragMouseDown;
   element.getElementsByClassName('content-window-file')[0].ontouchend=closeContentwindow;

   function dragMouseDown(e) {
      console.log(e.type);
      if(e.type==="touchstart"){
       //  closeContentwindow();
      }
      else {
         e.preventDefault();
         pos3 = e.clientX;
         pos4 = e.clientY;
         document.onmouseup = closeDragElement;
         document.onmousemove = elementDrag;
      }
   }

   function elementDrag(e) {
      let client_x=null;
      let client_y=null;
         client_x =e.clientX;
         client_y =e.clientY;
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
   }

}

function addEmoticonMenuListener(){
   const emoticonmenu=document.getElementById('emoticon_menu');
   const menuelement=document.getElementById('show_emoticon_menu');
   menuelement.addEventListener('click', (e)=>{
      document.getElementById('emoticon_menu').classList.add('open');
      console.log('opened');
      e.stopPropagation();
   });

   document.addEventListener('click',(e)=>{
      if(!emoticonmenu.contains(e.target)){
         emoticonmenu.classList.remove('open');
      }
   });
}

function addEmoticonMenuEvent(event){
   if(!event.target.closest('.emoticon-menu-btn, .emoticon-menu')){
      document.getElementById('emoticon_menu').style['display']='none';
   }
   console.log(event.target.closest)
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

function addEmoticonMenuButtons(){
   const menuelement=document.getElementById('emoticon_menu');
   let i=0;
   emoticon_list.forEach(emoticon=>{
      let btn=document.createElement('button');
      btn.id=i.toString();
      btn.className='emoticon-menu-btn';
      btn.textContent=emoticon.key;
      btn.title=emoticon.value;
      btn.type='button';
      btn.setAttribute('onclick','addTextEmoticon('+btn.id+')');
      //console.log(btn.alt)
      menuelement.appendChild(btn);
      i++;
   });
}

document.addEventListener('DOMContentLoaded', function(){
   const replyLinks = document.querySelectorAll('.reply-from a');
   const lazyImages = document.querySelectorAll('.lazy');
   addLazyLoadObserver();
   addLinkEventListener(replyLinks);
   addLazyLoadForFile(lazyImages);
   addContentwindowResizeEvent();
   addEmoticonMenuListener();
   changeAutoFormattedText();
   addEmoticonMenuButtons();
}, false);

/** CLOSE CONTENTWINDOW ON CLICK ON DOCUMENT
 document.addEventListener('click', function(event) {
   const contentWindow = document.getElementById('content_window');
   if (contentWindow.style.display === 'block' && !contentWindow.contains(event.target)) {
      closeContentWindow();
   }
});
 **/
