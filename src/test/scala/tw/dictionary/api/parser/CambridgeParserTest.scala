package tw.dictionary.api.parser

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class CambridgeParserTest extends FunSuite {
  test("Get test interpret") {
    val parser = new CambridgeParser
    assert(true)

    val context = """<script>(function() {YUI().use("swf","node","event",function(Y){var ele = Y.one("#audio1");if (ele) {var arg = {"fixedAttributes":{"allowScriptAccess":"always","name":"myflashObj","scale":"noscale","salign":"l","wmode":"transparent"},"flashVars":{"audio":"http://l.yimg.com/lc/mp3/94/20/87447.mp3"}};var noFlashPlayerMessage = ["請安裝","聽取即時發音"];Y.SWF(ele , "http://l.yimg.com/lc/swf/tw/audio_key_tw.swf", arg, 80, 21, noFlashPlayerMessage );}})})();(function() {YUI().use("dictionary",function(Y){(Y.Dictionary.tips("dictionary?o=json","很抱歉，字典找不到您要的資料喔！"));})})();(YUI().use("node",function(e){var c=e.all(".ask");var b=function(g){var f=g.currentTarget.next();if(f&&f.hasClass("tooltip")){f.addClass("on")}};var a=function(g){var f=g.currentTarget.next();if(f&&f.hasClass("tooltip")){f.removeClass("on")}};var d=function(f){f.preventDefault()};if(c){c.on("click",d);c.on("mouseover",b);c.on("mouseout",a)}}));YUI.add("dictionary",function(a){(function(){var c=a.UA,b=a.Lang;a.Dictionary=function(){var g=a.one("#qTip_c"),q,s,m=a.one("#qTip_h"),o=".example",l=".container-close,.rlcontent",i="qtip";var p;var k=function(t){g=a.one("#"+t)};var n=function(){return g};var h=function(t){m=a.one("#"+t)};var f=function(){return m};var e={start:function(u,t){},complete:function(v,u,t){},success:function(E,G,t){var D=a.JSON.parse(G.responseText);var H=D.results;var A="http://l.yimg.com/lc"+D.sound;var w=D.link;if(D){var v=t.success.oriQuery;var z=v.replace(".","");var F='<a href="'+w+'">'+z+"</a>";if(D.sound){F=F+'<cite id="audio"></cite>'}var x="<ul>";var u='<li><div class="pcixin">{wordtype}</div>\n\n<p>{eg}</p></li>';var B='<span class="not-found">?抱?，字典?不到?要的資料?！</span>';var L=D.results.length;var J=D.counts;if(J>0){for(var K=0;K<L;K++){x+=b.substitute(u,H[K])}x+="</ul>"}else{x+=B}var y=t.success.header;y.set("innerHTML",F);var C=y.next();C.set("innerHTML",x);if(D.sound){this.insertSWF(A)}t.success.container.setXY(s);t.success.container.setStyle("visibility","visible");var I=t.success.container;I.plug(a.Plugin.Drag);I.dd.addHandle("#"+t.success.header.get("id"))}},failure:function(v,u,t){},end:function(u,t){},insertSWF:function(t){YUI().use("swf","node","event",function(w){var v=w.one("#audio");if(v){var u={fixedAttributes:{allowScriptAccess:"always",name:"myflashObj",scale:"noscale",salign:"l",wmode:"transparent"},flashVars:{audio:t}};var x=["Please install","listen to the pronounciation"];w.SWF(v,"http://l.yimg.com/lc/swf/tw/audio_key_tw.swf",u,80,21,x)}})}};var j=function(x){var u=x.target;if(u.hasClass(i)){var w=n();if(a.Lang.trim(w.getStyle("visibility"))=="visible"){w.setStyle("visibility","hidden")}x.preventDefault();this._query=u.get("innerHTML");var v=p+"&p="+this._query;s=[u.getX()+u.get("offsetWidth")-140,u.getY()+u.get("offsetHeight")];var t={on:{start:e.start,complete:e.complete,success:e.success,failure:e.failure,end:e.end},context:e,headers:{"X-Transaction":"GET pop up window"},arguments:{start:"loading",complete:"loaded",success:{header:f(),container:w,oriQuery:this._query},failure:"failure",end:"ended"}};a.io(v,t)}};var d=function(u){var t=n();t.setStyle("visibility","hidden")};var r=function(t){t.each(function(){var y=this.get("innerHTML");var C=y;var w=y;var v=null;var u=null;var D=null;var A=/\<b\>.+\<\/b\>/gi;v=w.match(A);if(v!=null){u=v.toString();D=u.replace(/ /g,"");C=C.replace(u,D)}var z=/[^\<^\>][a-zA-Z-']{1,}[\s\.]/g;var B=C.match(z);if(null!=B){for(var x=0;x<B.length;x++){C=C.replace(B[x],'<a href="#" class="'+i+'">'+B[x]+"</a>")}C=C.replace(D,u);this.set("innerHTML",C)}})};return{_attachEvent:function(){var t=a.all(o);r(t);a.on("click",j,a.all(o+" a"));a.on("click",d,a.all(l))},tips:function(t){p=t;this._attachEvent()}}}()})()},"3.0.0",{requires:["node","io-base","json-parse","dd-plugin","substitute"]});YUI().use("node",function(Y) {var focusSearch = function(e) {Y.one("#yschs-dic").select();};Y.on("domready", focusSearch);});Y.use("srp",function(){Y.Search.SRP.init([],{"aria_loading_results":"Retrieving search results. Please wait.","aria_results_loaded":"New search results have been retrieved. Press control shift and down arrow key to jump to the first search result. Press control shift and up arrow key to jump back to the search box.","common.expando.playvideo":"Play Video","common.expando.closevideo":"Close Video","common.results.error":"Sorry, there was a problem retrieving search results. Please try again.","common.results.loading":"Loading results...","srp.title":"%q - Yahoo! Search Results"});});(function() {var sa = new YAHOO.Search.MiniAssist("yschs-dic", "atg", {"auto_positioning": true,"GOSSIP_CLASSNAME": "gossip","TRAY_CLASSNAME": "miniassist","aria_available_suggestions": "Search suggestions are available, use up and down arrows to review them.","aria_no_available_suggestions": "No suggestions are available."});sa.gossip.setConfigValue("url", "http://sugg.tw.search.yahoo.net/gossip-tw-pub_sayt?output=fxjsonp&droprotated=1&pubid=560&command=#{q}&queryfirst=1");sa.gossip.setConfigValue("linkTitle", "Search for#{q}");sa.gossip.setConfigValue("scrollSize", 10);sa.gossip.setConfigValue("linkParams", {"fr2" : "sg-gac"});})();</script>"""
    val httpRegex = """(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?""".r
    println(httpRegex.findFirstIn(context).get)
  }
}