<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

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

<tr><td colspan="2">Detect SDKs</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">One SDK per module.<br>
Can use the project SDK as the module SDK</td>
<td>❌</td><td>✅</td><td></td>

<tr><td colspan="2">Edit output folder, SDK, SDK properties</td>
<td>✅</td><td>✅</td><td>Copy of IntelliJ Project Structure</td>
</tr>

<tr><td colspan="2">Use a template<br>
(dune, Makefile, None)</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<!--
<tr><td colspan="2">Insight<br>(warnings/errors/...)</td>
<td>✅</td><td>✅</td><td> Build compile the current file and its dependencies every time the file was edited.</td>
</tr>
-->

</tbody>
</table>

Minor features

* SDKs are verified, a custom error message is shown if invalid or not supported by the plugin.
* Suggesting setting the OCaml SDK if not set (IntelliJ)

### REPL Console

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

<tr><td colspan="2">Can execute commands</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">Browse history,<br> use arrow up/down</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">See the values of the variables<br>
Can be hidden.</td>
<td>✅</td><td>✅</td><td>Can looks like OCamlJ</td>
</tr>

<tr><td colspan="2">Send a file to the console</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">Send the selected text to the console</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">Send the auto-detected statement to the console</td>
<td>✅</td><td>✅</td><td>You got a preview if there are multiple choices.</td>
</tr>

</tbody>
</table>

### Syntax / Highlighting

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

<tr><td colspan="2">Insight</td>
<td>✅</td><td>✅</td><td>(warnings, errors, alerts)</td>
</tr>

<tr><td colspan="2">Project-wide errors</td>
<td>❔</td><td>✅❔</td><td>(buggy)</td>
</tr>

</tbody>
</table>

Minor

* `odoc` comments are in green (🙄)

### Utilities

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

<tr><td colspan="2">Button to the documentation/API</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">File templates</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td colspan="2">Show type<br>
(CTRL+SHIFT+P)</td>
<td>✅</td><td>✅</td><td></td>
</tr>

</tbody>
</table>

### Support for .annot

The compiler is generating .annot files with the option `-annot` (deprecated in 4.13 but not removed yet). You can

* see the parsed .annot next to the source
* click on the parsed tree ➡️ focus the matching element in the view
* click on the view ➡️ focus the matching entry in the parsed tree

### ReasonML

As the plugin is based on the OCaml part of the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) here are the features that were partially or completely imported from the ReasonML plugin which is also based on another plugin called [ocaml-ide](https://github.com/sidharthkuruvila/ocaml-ide).

* Highlighting + Parser
* File Structure menu
* Autocompletion
* Go to file, declaration, etc.
* Comment line/block
* Insert matching brace, quote
* Find usages
* Preview `odoc` documentation (CTRL-Q / hover)
* Support for Dune