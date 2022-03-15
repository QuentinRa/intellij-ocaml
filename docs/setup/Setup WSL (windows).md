<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

**WSLs are only supported since 2021.1 (newer than 211)**.

### 1. Open the Windows Store

<img src="https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/a3e2b688-60c2-4482-880d-2e274c62cd17.png" alt="Start Microsoft Store" />

### 2. Search **Ubuntu** or **Debian**

### 3. Click on "install" to install your WSL

<img src="https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/ae490a55-fd79-4bf1-af21-16a180a2a714.png" alt="Install Ubuntu WSL" />

### 4. In Windows search bar, enter **Ubuntu** (resp. **Debian**)

<img src="https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/17232a06-8e7b-4d79-919f-503b253e6985.png" alt="Start Ubuntu WSL" />

### 5. Enter a username, and your password

Note: there is no "echo" of your password, so you are in "blind mode".

<img src="https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/e395b91c-b9ba-457c-a00a-33f59acdb4b7.png" alt="configure new WSL" />

### 6. Update packages (note: press Enter to run a command)

Note: you will have to write your password. You may use another package manager aside "apt-get".

<ol>
<li><code>sudo apt-get update</code>(update your list of packages)</li>
<li><code>sudo apt-get upgrade</code>(upgrade your packages)</li>
</ol>

### 7. Install OCaml (you can install both native and opam)

Note: you will have to write your password. You may use another package manager aside "apt-get".

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