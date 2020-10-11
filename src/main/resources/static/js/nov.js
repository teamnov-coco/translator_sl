
const {fromEvent, Observable, from, of} = rxjs;
const {map, switchMap, filter, distinctUntilChanged, tap,debounceTime,merge,mergeMap,finalize, pluck} = rxjs.operators;
const $chatContent = document.getElementById("chatContent");
const $chatOpenBtn = document.getElementById("chatOpenBtn");
const $chatCloseBtn = document.getElementById("chatCloseBtn");
const $langChangeBtn = document.getElementById("langChangeBtn");
const $tranTextarea = document.getElementById("tranTextarea");
const $tranBtn = document.getElementById("tranBtn");
const $tranContent = document.getElementById("tranContent");
const $tranNoticeClone = document.getElementById("tranNoticeClone");
const $tranWrapClone = document.getElementById("tranWrapClone");
const $tranHisContent = document.getElementById("tranHisContent");
const $tranHisClone = document.getElementById("tranHisClone");
const $tranSourceLng = document.getElementById("sourceLng");
const $tranTargetLng = document.getElementById("targetLng");
const $guideContent = document.getElementById("guideContent");

import {diffString3} from '/js/diff.js';

var tranHistoryArray = {}; 
var tranHistory = window.localStorage;
var tranHistoryCount = tranHistory.length;
// tranHistory.clear();


//음성인식
var voices = [];
function setVoiceList() {
    voices = window.speechSynthesis.getVoices();
}
setVoiceList();
if (window.speechSynthesis.onvoiceschanged !== undefined) {
    window.speechSynthesis.onvoiceschanged = setVoiceList;
}

function speech(id,lang,com) {
    if(!window.speechSynthesis) {
        alert("음성 재생을 지원하지 않는 브라우저입니다. 크롬, 파이어폭스 등의 최신 브라우저를 이용하세요");
        return;
    }
    var msg= JSON.parse(tranHistory.getItem(id))[com].data;

    lang = detectCode2LngTts(lang);
    var utterThis = new SpeechSynthesisUtterance(msg);
    utterThis.onend = function (event) {
        // console.log('end');
    };
    utterThis.onerror = function(event) {
        console.log('error', event);
    };
    // 브라우저마다 특정 보이스로 하면 중간에 끊키고 오류가나옴 현재는 기본 보이스로 가동하고 나중에 찾기
    // var voiceFound = false;
    // for(var i = 0; i < voices.length ; i++) {
    //     if(voices[i].lang.indexOf(lang) >= 0 || voices[i].lang.indexOf(lang.replace('-', '_')) >= 0) {
    //         utterThis.voice = voices[i];
    //         voiceFound = true;
    //     }
    // }
    // if(!voiceFound) {
    //     alert('voice not found');
    //     return;
    // }
    utterThis.lang = lang;
    utterThis.pitch = 1;
    utterThis.rate = 1; //속도
    window.speechSynthesis.speak(utterThis);
}


function tranFomatWrap(tranData){
    let elem = $tranWrapClone; 
    let clone = elem.cloneNode(true); 
    clone.classList.add(`${tranData.position}`)
    clone.getElementsByClassName("msg-subscript")[0].innerHTML = `<i class="${tranData.senderClass}">${tranData.sender}</i>`;
    clone.getElementsByClassName("msg-shape")[0].innerHTML = `${tranData.dataDiv}`;
    clone.getElementsByClassName("nov-tts")[0].dataset.id = `${tranData.hisName}`;
    clone.getElementsByClassName("nov-tts")[0].dataset.lang = `${tranData.targetLngCode}`;
    clone.getElementsByClassName("nov-tts")[0].dataset.com = `${tranData.sender}`;
    $tranContent.appendChild(clone);
    $tranContent.scrollTo(0, $tranContent.scrollHeight);
}

function tranFomatNotice(tranData){
    let elem = $tranNoticeClone; 
    let clone = elem.cloneNode(true); 
    clone.getElementsByClassName("msg-notice-text")[0].innerHTML = `${detectCode2Lng(tranData.sourceLngCode)} <i class="nov-right-big"> ${detectCode2Lng(tranData.targetLngCode)}`;
    $tranSourceLng.value = tranData.sourceLngCode;
    $tranTargetLng.value = tranData.targetLngCode;
    $tranContent.appendChild(clone);
    $tranContent.scrollTo(0, $tranContent.scrollHeight);
}

function tranFomatHis(tranData){
    let elem = $tranHisClone; 
    let clone = elem.cloneNode(true); 
    
    clone.getElementsByTagName("img")[0].src = `/img/${tranData.sourceLngCode}.png`;
    clone.getElementsByClassName("history-body-lang")[0].innerHTML = `${tranData.sourceLng} <i class="nov-right-big"> ${tranData.targetLng}`;
    clone.getElementsByClassName("history-body-text")[0].innerHTML = `${tranData.data}`;

    for (let i = 0; i < $tranHisContent.children.length; i++) {
        $tranHisContent.children[i].classList.remove('select');
    }
    clone.classList.add(`select`)

    clone.dataset.id=tranData.hisName;
    for (let i = 0; i < clone.children.length; i++) {
        clone.children[i].dataset.id=tranData.hisName;
    }
    clone.getElementsByClassName("history-body-lang")[0].dataset.id=tranData.hisName;
    clone.getElementsByClassName("history-body-text")[0].dataset.id=tranData.hisName;
    clone.getElementsByClassName("nov-right-big")[0].dataset.id=tranData.hisName;
    // $tranHisContent.appendChild(clone);
    $tranHisContent.insertBefore(clone,$tranHisContent.children[0]);
    $tranHisContent.scrollTo(0,0);
}

function tranHistoryPush(tranData){
    var pushData ={
        data: tranData.data,
        dataDiv: tranData.dataDiv,
        sender : tranData.sender,
        senderClass : tranData.senderClass,
        position: tranData.position,
        sourceLng: detectCode2Lng(tranData.sourceLngCode),
        targetLng: detectCode2Lng(tranData.targetLngCode),
        sourceLngCode: tranData.sourceLngCode,
        targetLngCode: tranData.targetLngCode,
        hisName: tranData.hisName
    }
    tranHistoryArray[pushData.sender] = pushData;
}

function tranHistorySet(){
    //session 저장
    var hisCount = Number(tranHistory.length);
    tranHistory.setItem(`his${hisCount}`,`${JSON.stringify(tranHistoryArray)}`);
    var data = {
        data: tranHistoryArray['user'].data,
        dataDiv: tranHistoryArray['user'].data,
        position: tranHistoryArray['user'].position,
        sender: tranHistoryArray['user'].sender,
        senderClass: tranHistoryArray['user'].senderClass,
        sourceLng: tranHistoryArray['user'].sourceLng,
        sourceLngCode: tranHistoryArray['user'].sourceLngCode,
        targetLng: tranHistoryArray['user'].targetLng,
        targetLngCode: tranHistoryArray['user'].targetLngCode,
        hisName: `his${hisCount}`
    }
    
    //history ui
    $guideContent.style.display = 'none';
    tranFomatHis(data);
    tranHistoryArray = {};
}

function tranHistorySelect(id){
    chatOpen();
    for (let i = 0; i < $tranHisContent.children.length; i++) {
        $tranHisContent.children[i].classList.remove('select');
    }
    for (let i = 0; i < $tranHisContent.children.length; i++) {
        if($tranHisContent.children[i].dataset.id == id){
            $tranHisContent.children[i].classList.add('select');
        };
    }
}

function chatOpen(){
    $chatContent.classList.add('show');
}

function chatClose(){
    $chatContent.classList.remove('show');
}

function langChange(){
    var sourceTmep = $tranSourceLng.value;
    if( sourceTmep != 'de'){
        $tranSourceLng.value = $tranTargetLng.value;
        $tranTargetLng.value = sourceTmep;
    }
}

function detectCodeMax(msg){
    if($tranSourceLng.value == "de"){
        var msgArray = msg.split("");
        var sortArray = [];
        var msgCount = msgArray.reduce((object, currentValue) => {
            var detect = detectCode(currentValue);
            if (!object[detect]) {
                object[detect] = 0;
            }
            object[detect]++;
            
            return object;
        },{});
        
        for(const [key, value] of Object.entries(msgCount)) {
            sortArray.push(JSON.parse(`{"key":"${key}","value":"${value}"}`));
        };
        
        sortArray.sort((a, b) => {
            if(Number(a.value) == Number(b.value)){ return 0} return Number(a.value) > Number(b.value) ? -1 : 1;
        });
        return sortArray[0].key;
    }else{
        return $tranSourceLng.value
    }
}

function detectCode(msg){
    var code = '000' + msg.codePointAt(0).toString(16);
    code = code.substring(code.length - 4);
    if(code >= 'ac00' && code <= 'd800'){
        //한국
        return 'ko';
    }else if(code >= '0041' && code <= '007a'){
        //영어
        return 'en';
    }else if(code >= '4e00' && code <= '9fbf'){
        //중국어
        return 'cn';
    }else if((code >= '3040' && code <= '30ff') || code >= '31f0' && code <= '31ff'){
        //일본어
        return 'ja';
    }else{
        return 'ko';
    }
}

function detectCode2Lng(code){
    if(code == 'ko'){
        //한국
        return '한국어';
    }else if(code == 'en'){
        //영어
        return '영어';
    }else if(code == 'cn'){
        //중국어
        return '중국어';
    }else if(code == 'ja'){
        //일본어
        return '일본어';
    }else{
        return 'ko';
    }
}

function detectCode2LngTts(code){
    if(code == 'ko'){
        //한국
        return 'ko-KR';
    }else if(code == 'en'){
        //영어
        return 'en-US';
    }else if(code == 'cn'){
        //중국어
        return 'zh-CN';
    }else if(code == 'ja'){
        //일본어
        return 'ja-JP';
    }else{
        return 'ko';
    }
}


const tranHistoryList$ = Observable.create(function(observer){
    if(tranHistoryCount != 0){
        $guideContent.style.display = 'none';
        for (let i = 0; i < tranHistoryCount; i++) {
            var hisData = JSON.parse(tranHistory.getItem(`his${i}`)).user;
            var setData = {
                data: hisData.data,
                dataDiv: hisData.dataDiv,
                position: hisData.position,
                sender: hisData.sender,
                senderClass: hisData.senderClass,
                sourceLng: hisData.sourceLng,
                sourceLngCode: hisData.sourceLngCode,
                targetLng: hisData.targetLng,
                targetLngCode: hisData.targetLngCode,
                hisName: `his${i}`
            }
            observer.next(setData);
        }
    }
    observer.complete();
}).subscribe(next => {
    tranFomatHis(next);
});


const sendTran$ =  tranData => Observable.create(function(observer){
    var hisCount = Number(tranHistory.length);
    var sendData = {
        data: tranData.msg,
        dataDiv: tranData.msg,
        sender : 'user',
        senderClass : 'nov-user',
        position: 'user',
        sourceLngCode: tranData.sourceLngCode,
        targetLngCode: tranData.targetLngCode
    }
    observer.next(sendData);

    //번역기 인터페이스
    // {
    // 	"com": "[kakao, naver, google]" 							            //번역기 구분
    // 	"cde": "[err, ok]"											            //코드 구분
    // 	"msg": "[(cde==err ? 에러메시지스트링), (cde==ok) ? 결과값 스트링]"		  //결과메시지
    // }

    var firstData = '';
    var url = `/${tranData.sourceLngCode}2${tranData.targetLngCode}?txt=${encodeURIComponent(tranData.msg)}`;
    var source = new EventSource(url);
    source.onmessage = function(ev) {
        var object = JSON.parse(ev.data);
        if(object.cde == 'ok'){
            sendData.data = object.msg;
            if(firstData != ''){
                sendData.dataDiv = diffString3(firstData,object.msg);
            }else{
                sendData.dataDiv = object.msg;
                firstData = object.msg;
            }
            sendData.sender = object.com;
            sendData.senderClass = object.com;
            sendData.position = 'sys';
            sendData.hisName= `his${hisCount}`;
            observer.next(sendData);
        }else{
            //err코드
            // console.log(object.msg);
        }
    };

    source.onerror = function(err) {
        if (source != null) {
            source.close();
            source = null;
        }
        tranHistorySet();
        observer.complete();
    };
});



const keyDownTran$ = fromEvent($tranTextarea,"keydown")
.pipe(
    map(e => e.keyCode),
    distinctUntilChanged(),
    filter(e => 13 == e)
)

const clickTran$ = fromEvent($tranBtn,"click")
.pipe(
    merge(keyDownTran$),
    map(e => $tranTextarea.value),
    filter(e => 1 <= e.length),
    distinctUntilChanged(),
    map(e => ({msg : e, sourceLngCode: detectCodeMax(e), targetLngCode: $tranTargetLng.value})),
    filter(e => e.sourceLngCode != e.targetLngCode),
    tap(e => tranFomatNotice(e)),
    switchMap(e => sendTran$(e)),
    tap(e => $tranTextarea.value = "")
);

clickTran$.subscribe(
    tranData => {
        tranFomatWrap(tranData);
        tranHistoryPush(tranData);
    }
);

const clickTranHis$ = fromEvent($tranHisContent,"click")
.pipe(
    pluck("target","dataset","id"),
    filter(e => e != null),
    tap(e => tranHistorySelect(e)),
    mergeMap(e => of(JSON.parse(tranHistory.getItem(e))))
);

clickTranHis$.subscribe(
    e => { 
        for(const [key, value] of Object.entries(e)) {
            if(key == "user"){
                tranFomatNotice(value);
            }
            tranFomatWrap(value);
        }
    }
);

const chatCloseBtn$ = fromEvent($chatCloseBtn,"click")
.pipe(
    tap(e => chatClose())
).subscribe();

const langChangeBtn$ = fromEvent($langChangeBtn,"click")
.pipe(
    tap(e => langChange())
).subscribe();

const chatOpenBtn$ = fromEvent($chatOpenBtn,"click")
.pipe(
    tap(e => chatOpen())
).subscribe();


const clickTts$ = fromEvent($chatContent,"click")
.pipe(
    pluck("target","dataset"),
    filter(e => e.id != null),
);

clickTts$.subscribe(
    e => {
        speech(e.id,e.lang,e.com);
    }
)