const {fromEvent, Observable} = rxjs;
const {map, switchMap, filter, distinctUntilChanged, tap} = rxjs.operators;
const $tranval = document.getElementById("tranval");
const $tranlist = document.getElementById("tranlist");
const $trancontent = document.getElementById("trancontent");
// const $tranTargetLng = document.getElementById("targetLng");


function tranfomat(msg,sender,position){
        let elem = $trancontent; 
        let clone = elem.cloneNode(true); 
        clone.classList.add(`message-box-${position}`)
        clone.getElementsByClassName("message-lang")[0].innerHTML = `<span>${sender}</span>`;
        clone.getElementsByClassName("message-text")[0].innerHTML = `${msg}`;
        $tranlist.appendChild(clone);
        window.scrollTo(0, document.body.scrollHeight);
        // console.table(document.body);
}

function detest(msg){
    var test = msg.split("");
    var array = [];
    var asdf = test.reduce((object, currentValue) => {
        var detect = detectMsg(currentValue);
        if (!object[detect]) {
            object[detect] = 0;
        }
        object[detect]++;
        
        return object;
    },{});
    
    for(const [key, value] of Object.entries(asdf)) {
        array.push(`{key:${key},value:${value}}`);
    };

    array.sort((a, b) => {
        return (a.value < b.value) ? -1 : (a.value > b.value) ? 1: 0;
    });

    console.log(array[0].value);

}

function detectMsg(msg){
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

// function duplicationLng(msg){
//     var targetLng = $tranTargetLng.value;
//     var duplication = detectMsg(msg) == targetLng;
//     return duplication;
// }

const sendTran$ =  msg => Observable.create(function(observer){
    //소스 코드 언어감지
    var sourceLng = detectMsg(msg);
    // var targetLng = $tranTargetLng.value;

    //데이터 적재
    tranData = {
        data: msg,
        sender : '사용자',
        position: 'right',
        sourceLng: sourceLng,
        // targetLng: targetLng
    }
    observer.next(tranData);

    //url인코딩하기

    //번역기 인터페이스
    // {
    // 	"com": "[kakao, naver, google]" 							//번역기 구분
    // 	"cde": "[err, ok]"											//코드 구분
    // 	"msg": "[(cde==err ? 에러메시지스트링), (cde==ok) ? 결과값 스트링]"		//결과메시지
    // }

    //스트림 열기
    var url = '';
    if(sourceLng === 'ko'){
        url = `/kor2eng?txt=${msg}`; 
    }else if(sourceLng === 'en'){
        url = `/eng2kor?txt=${msg}`;
    }
    var source = new EventSource(url);

    source.onmessage = function(ev) {
        var object = JSON.parse(ev.data);
        tranData.data = object.msg;
        tranData.sender = object.com;
        tranData.position = 'left';
        observer.next(tranData);
    };

    source.onerror = function(err) {
        observer.complete();
        if (source != null) {
            source.close();
            source = null;
        }
    };
});


const clickTran$ = fromEvent(document.getElementById("tranbtn"),"click")
.pipe(
    map(e => $tranval.value),
    filter(e => 1 <= e.length),
    distinctUntilChanged(),
    // map(e => ({test1 : e, test2: detest(e)})),
    // tap(e => console.log(e)),
    // filter(e => false === duplicationLng(e)),
    switchMap(e => sendTran$(e)),
);

clickTran$.subscribe(tranData => {
    tranfomat(tranData.data, tranData.sender, tranData.position);
});