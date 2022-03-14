<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

### 1. Native or Opam SDK?

To use the plugin, you will need to install ocaml first. You will need to pick whether you will use a **native** SDK, or an **opam** SDK. Opam is providing more complete SDKs, so some features won't be available for people using a **native SDK** (I'm doing my best so that this won't impact much your experience trough).

<table>
<thead>
<tr>
<th>Feature</th>
<th>Native</th>
<th>Opam<br>(recommended)</th>
<th>Note</th>
</tr>
</thead>

<tbody>

<tr><td>IntelliJ</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>Minor IDEs (CLion, ...)</td>
<td>❌</td><td>✅</td><td>Interface not complete yet</td>
</tr>

<tr><td></td></tr>

<tr><td>Autocompletion</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>Compilation on change</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>REPL</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>.annot editor</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>Type inference</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>Multiples OCaml SDKs</td>
<td>❌</td><td>✅</td><td>This is a feature of opam</td>
</tr>

[//]: # (<tr><td>Reformat code</td>)

[//]: # (<td>❌</td><td>✅</td><td>Not implemented yet</td>)

[//]: # (</tr>)

</tbody>
</table>

### 2. Pick your provider

Once you know which kind of SDK you want, you need to look for a provider, something, that you will use to install your ocaml SDKs. I'm directly supporting these (you may request for more providers)

<table>
<thead>
<tr>
<th></th>
<th>Native</th>
<th>Opam</th>
<th>Note</th>
</tr>
</thead>

<tbody>
<tr><td>WSL - Windows</td>
<td>✅</td><td>✅</td><td>Linux on Windows (Linux console on Windows using Docker)</td>
</tr>

<tr><td>Cygwin - Windows</td>
<td>✅</td><td>✅</td><td>Linux executables on Windows (ex: ocaml => ocaml.exe)</td>
</tr>

<tr><td>OCaml64 - Windows</td>
<td>❌</td><td>✅</td><td>Cygwin+opam "one-click" installer</td>
</tr>

<tr><td>Linux</td>
<td>✅</td><td>✅</td><td></td>
</tr>

<tr><td>macOS</td>
<td>❔</td><td>❔</td><td>Not tested</td>
</tr>

</tbody>
</table>