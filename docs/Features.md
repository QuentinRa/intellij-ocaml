Here is the list of features from the OCaml plugin

### Setup

<table>
<thead>
<tr>
<th colspan="2">Feature</th>
<th>IntelliJ</th>
<th>Minor IDEs</th>
<th>Note</th>
</tr>
</thead>

<tbody>
<tr><td colspan="2">Setup (opam SDK)</td>
<td>✅</td><td>✅</td><td></td>
</tr>
<tr><td colspan="2">Setup (native SDK)</td>
<td>❌</td><td>✅</td><td>Interface not available in minors IDEs for now.</td>
</tr>

<!--
<tr><td colspan="2">Insight<br>(warnings/errors/...)</td>
<td>✅</td><td>✅</td><td> Build compile the current file and its dependencies everytime the file was edited.</td>
</tr>
-->

</tbody>
</table>


### ReasonML

As the plugin is based on the OCaml part of the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) here is the features that were partially or completely imported from the ReasonML plugin.

* File Structure menu
* Autocompletion
* Go to file, declaration, etc.
* Comment line/block
* Insert matching brace, quote
* Find usages
* Preview `odoc` documentation (CTRL-Q / hover)