<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

### 1. Open the Windows Store

<img src="https://plugins.jetbrains.comhttps://plugins.jetbrains.com/files/18531/1253-page/a3e2b688-60c2-4482-880d-2e274c62cd17" alt="" />

### 2. Search **Ubuntu** or **Debian**

### 3. Click on "install" to install your WSL


<img src="https://plugins.jetbrains.com/files/18531/1253-page/ae490a55-fd79-4bf1-af21-16a180a2a714" alt="" />

### 4. In Windows search bar, enter **Ubuntu** (resp. **Debian**)

<img src="https://plugins.jetbrains.com/files/18531/1253-page/17232a06-8e7b-4d79-919f-503b253e6985" alt="" />

### 5. **Enter** a username, and your password

Note: nothing is shown on the screen ~ blind mode.

<img src="https://plugins.jetbrains.com/files/18531/1253-page/e395b91c-b9ba-457c-a00a-33f59acdb4b7" alt="" />

### 6. Update packages (note: press **Enter** to run a command)

<ol>
<li><code>sudo apt-get update</code>(update your list of packages)</li>
<li><code>sudo apt-get upgrade</code>(upgrade your packages)</li>
</ol>

### 7. **Install** OCaml (you can install both native and opam)

<ul>
<li>native: <code>sudo apt-get install ocaml</code></li>
<li>opam (<b>recommended</b>):
<ol>
<li><code>sudo apt-get install opam</code></li>
<li><code>opam init</code> (enter <code>y</code> when prompted)</li>
<li>then <code>eval $(opam env)</code></li>
</ol>
</li>
</ul>

### 8. Test

If `ocaml -vnum` outputs your ocaml version, then you are done ✅.