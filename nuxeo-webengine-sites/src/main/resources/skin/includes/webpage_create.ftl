<#macro webPageCreate>
<!-- markit up -->
<script src="${skinPath}/script/markitup/jquery.markitup.pack.js"></script>
<script src="${skinPath}/script/markitup/sets/wiki/set.js"></script>
<link rel="stylesheet" type="text/css" href="${skinPath}/script/markitup/skins/markitup/style.css" />
<link rel="stylesheet" type="text/css" href="${skinPath}/script/markitup/sets/wiki/style.css" />
<!-- end markitup -->

<script type="text/javascript">
	
	function onSelectRadio(obj)
	{
	
		alert(oFCKeditor.Value);
		
		if(obj.id == "wikitext")
		{
			var wiki = document.getElementById('wikitextArea');
			if(wiki){
				wiki.style.display = 'block';
				wiki.style.zIndex = '10000';
			}
			else{
				alert('Oups problem getting component !');
			}
			
			var rich = document.getElementById('richtextArea');
			if(rich){
				rich.style.display = 'none';
				rich.style.zIndex = '1';
			}
			else{
				alert('Oups problem getting component !');
			}
		}	
		
		else
		{
			var rich = document.getElementById('richtextArea');
			if(rich){
				rich.style.display = 'block';
				rich.style.zIndex = '10000';
			}
			else{
				alert('Oups problem getting component !');
			}
			
			var wiki = document.getElementById('wikitextArea');
			if(wiki){
				wiki.style.display = 'none';
				wiki.style.zIndex = '10000';
			}
			else{
				alert('Oups problem getting component !');
			}
		}
	}

</script>

<form method="POST" action="${This.path}/createWebPage" accept-charset="utf-8">
  <table class="createWebPage">
    <tbody>
      <tr>
        <td width="30%"></td>
        <td width="70%"></td>
      </tr>
      <tr>
        <td>${Context.getMessage("label.page.title")}</td>
        <td><input type="text" name="title" value=""/></td>
      </tr>
      <tr>
        <td>${Context.getMessage("label.page.description")}</td>
        <td><textarea name="description"></textarea></td>
      </tr>
      <tr>
        <td>${Context.getMessage("label.page.format")}</td>
        <td>
          <input type="radio" onclick="onSelectRadio(this);" name="format" id="wikitext" value="wikitext" checked="true" />${Context.getMessage("label.page.format.wikitext")}
          <input type="radio" onclick="onSelectRadio(this);" name="format" id="richtext" value="richtext" />${Context.getMessage("label.page.format.richtext")}
        </td>
      </tr>
      <tr> 
        <td colspan="2">
          <div id="wikitextArea">
            <textarea name="wikitextEditor" cols="60" rows="20" id="wiki_editor" class="entryEdit"></textarea>
          </div> 
          <div id="richtextArea" style='display: none;'>
            <textarea name="richtextEditor" cols="60" rows="20" id="richtextEditor" class="entryEdit"></textarea> 
          </div> 
        </td>
      </tr>
      <tr>
        <td>${Context.getMessage("label.page.push")}</td>
        <td>
          <input type="radio" name="pushToMenu" value="true" checked="true" />${Context.getMessage("label.page.push.yes")}
          <input type="radio" name="pushToMenu" value="false" />${Context.getMessage("label.page.push.no")}
        </td>
      </tr>
      <tr>
        <td colspan="2"><input type="submit" value="Save"/></td>
      </tr>
    </tbody>
  </table>  
</form>

<script>
function launchEditor() {
  $('#wiki_editor').markItUp(myWikiSettings);
}

$('#wiki_editor').ready(function() {
  setTimeout(launchEditor, 10);
});
</script>

</#macro>
