const {fromEvent, Observable, from} = rxjs;
const {map, switchMap, filter, distinctUntilChanged, tap,debounceTime,merge,mergeMap} = rxjs.operators;
const $tranTextarea = document.getElementById("tranTextarea");
const $tranBtn = document.getElementById("tranBtn");
const $tranContent = document.getElementById("tranContent");
const $tranNoticeClone = document.getElementById("tranNoticeClone");
const $tranWrapClone = document.getElementById("tranWrapClone");
const $tranSourceLng = document.getElementById("sourceLng");
const $tranTargetLng = document.getElementById("targetLng");

const tranHistory = window.sessionStorage;
const tranHistoryCount = tranHistory.length;

for (let index = 0; index < 4; index++) {
    tranHistory.setItem(`${index}번인데`,`${index}번값인데`);    
}

var asdfasdf = {
    test1 : 'tttt',
    test2 : 'tttt2',
    test3 : 'tttt3',
    test4 : 'tttt4',
}

console.log(asdfasdf);
tranHistory.setItem(`test`,`${asdfasdf.test2}`);
// tranHistory.clear();
console.log(tranHistory.getItem("test"));

for (let index = 0; index < tranHistory.length; index++) {
    console.log(tranHistory.key(index));
}



function tranFomatWrap(msg,sender,senderClass,position){
    let elem = $tranWrapClone; 
    let clone = elem.cloneNode(true); 
    clone.classList.add(`${position}`)
    clone.getElementsByClassName("msg-subscript")[0].innerHTML = `<i class="${senderClass}">${sender}</i>`;
    clone.getElementsByClassName("msg-shape")[0].innerHTML = `${msg}`;
    $tranContent.appendChild(clone);
    window.scrollTo(0, document.body.scrollHeight);
}

function tranFomatNotice(tranData){
    let elem = $tranNoticeClone; 
    let clone = elem.cloneNode(true); 
    clone.getElementsByClassName("msg-notice-text")[0].innerHTML = `언어감지(${detectCode2Lng(tranData.sourceLngCode)}) -> ${detectCode2Lng(tranData.targetLngCode)}`;
    $tranContent.appendChild(clone);
    window.scrollTo(0, document.body.scrollHeight);
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
            console.log(a.value);
            // console.log(b.value);
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

const sendTran$ =  tranData => Observable.create(function(observer){
    sendData = {
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

    var url = `/${tranData.sourceLngCode}2${tranData.targetLngCode}?txt=${tranData.msg}`;
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
    tap(e => console.log(e)),
    filter(e => e.sourceLngCode != e.targetLngCode),
    tap(e => tranFomatNotice(e)),
    switchMap(e => sendTran$(e)),
    tap(e => $tranTextarea.value = ""),
    
);

clickTran$.subscribe(
    tranData => {
        tranFomatWrap(tranData.data, tranData.sender,tranData.senderClass, tranData.position)
    }
);