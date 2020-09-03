const {fromEvent, Observable, from} = rxjs;
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


var tranHistoryArray = []; 
var tranHistory = window.localStorage;
var tranHistoryCount = tranHistory.length;
// tranHistory.clear();

function tranFomatWrap(tranData){
    let elem = $tranWrapClone; 
    let clone = elem.cloneNode(true); 
    clone.classList.add(`${tranData.position}`)
    clone.getElementsByClassName("msg-subscript")[0].innerHTML = `<i class="${tranData.senderClass}">${tranData.sender}</i>`;
    clone.getElementsByClassName("msg-shape")[0].innerHTML = `${tranData.data}`;
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
    $tranHisContent.appendChild(clone);
    $tranHisContent.scrollTo(0, $tranHisContent.scrollHeight);
}

function tranHistoryPush(tranData){
    var pushData ={
        data: tranData.data,
        sender : tranData.sender,
        senderClass : tranData.senderClass,
        position: tranData.position,
        sourceLng: detectCode2Lng(tranData.sourceLngCode),
        targetLng: detectCode2Lng(tranData.targetLngCode),
        sourceLngCode: tranData.sourceLngCode,
        targetLngCode: tranData.targetLngCode
    }
    tranHistoryArray.push(pushData);
}

function tranHistorySet(){
    //session 저장
    hisCount = Number(tranHistory.length);
    tranHistory.setItem(`his${hisCount}`,`${JSON.stringify(tranHistoryArray)}`);
    var data = {
        data: tranHistoryArray[0].data,
        position: tranHistoryArray[0].position,
        sender: tranHistoryArray[0].sender,
        senderClass: tranHistoryArray[0].senderClass,
        sourceLng: tranHistoryArray[0].sourceLng,
        sourceLngCode: tranHistoryArray[0].sourceLngCode,
        targetLng: tranHistoryArray[0].targetLng,
        targetLngCode: tranHistoryArray[0].targetLngCode,
        hisName: `his${hisCount}`
    }
    
    //history ui
    guideContent.style.display = 'none';
    tranFomatHis(data);
    tranHistoryArray = [];
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


const tranHistoryList$ = Observable.create(function(observer){
    if(tranHistoryCount != 0){
        guideContent.style.display = 'none';
        for (let i = 0; i < tranHistoryCount; i++) {
            var hisData = JSON.parse(tranHistory.getItem(`his${i}`))[0];
            var setData = {
                data: hisData.data,
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
    var sendData = {
        data: tranData.msg,
        sender : '사용자',
        senderClass : 'nov-user',
        position: 'user',
        sourceLngCode: tranData.sourceLngCode,
        targetLngCode: tranData.targetLngCode
    }
    observer.next(sendData);

    //번역기 인터페이스
    // {
    // 	"com": "[kakao, naver, google]" 							//번역기 구분
    // 	"cde": "[err, ok]"											//코드 구분
    // 	"msg": "[(cde==err ? 에러메시지스트링), (cde==ok) ? 결과값 스트링]"		//결과메시지
    // }

    var url = `/${tranData.sourceLngCode}2${tranData.targetLngCode}?txt=${encodeURIComponent(tranData.msg)}`;
    var source = new EventSource(url);
    source.onmessage = function(ev) {
        var object = JSON.parse(ev.data);
        sendData.data = object.msg;
        sendData.sender = object.com;
        sendData.senderClass = object.com,
        sendData.position = 'sys';
        observer.next(sendData);
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
        tranFomatWrap(tranData),
        tranHistoryPush(tranData)
    }
);

const clickTranHis$ = fromEvent($tranHisContent,"click")
.pipe(
    pluck("target","dataset","id"),
    filter(e => e != null),
    tap(e => tranHistorySelect(e)),
    mergeMap(e => from(JSON.parse(tranHistory.getItem(e))))
);

clickTranHis$.subscribe(
    e => { 
        if(e.position == "user"){
            tranFomatNotice(e);
        }
        tranFomatWrap(e);
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
