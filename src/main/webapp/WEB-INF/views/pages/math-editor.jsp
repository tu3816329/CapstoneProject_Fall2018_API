<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
       "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<script src="https://www.wiris.net/demo/editor/editor"></script>
<script>
	var editor;
	window.onload = function () {
		editor = com.wiris.jsEditor.JsEditor.newInstance({'language': 'en'});
		editor.insertInto(document.getElementById('editorContainer'));
	}
</script>
	   
<button style="display: none" id="btn-save"></button>
<div id="editorContainer"></div>
	   
<script>
	$('#btn-save').click(function() {
		Android.sendData(editor.getMathML());
	});

	function displayMathML(data) {
		editor.setMathML(data);
	}
</script>