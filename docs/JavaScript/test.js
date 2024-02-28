var  xhr = new XMLHttpRequest()
xhr.open('GET','http://baidu.com',true)
xhr.onreadystatechange = function (){
    if (xhr.readyState == 4 && xhr.status == 200){
        var text = xhr.responseText
        console.log(text);
    }
}