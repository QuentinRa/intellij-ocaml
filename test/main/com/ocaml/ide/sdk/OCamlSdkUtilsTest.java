package com.ocaml.ide.sdk;

import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import com.ocaml.*;
import org.junit.*;

/**
 * This class is testing a lot of values for the method
 * {{@link OCamlSdkUtils#createSdk(String, String, String, String)}}.
 * We need to handle every kind of data properly.
 * <ul>
 *     <li><b>KO</b>: WSL paths</li>
 *     <li><b>KO</b>: Windows paths</li>
 *     <li><b>KO</b>: Linux paths</li>
 * </ul>
 * By handling, we mean that we need to check that given a wrong location for
 * the ocaml binary or the library, we are returning a proper error message. If everything
 * is OK, we need to ensure that the SDK was created properly.
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlSdkUtilsTest extends OCamlBaseTest {

    // assuming that ocaml IS installed on this path
    protected static final String V_OCAML_BINARY_WSL = "\\\\wsl$\\Debian\\bin\\ocaml";
    protected static final String V_OCAML_COMPILER_WSL = "\\\\wsl$\\Debian\\bin\\ocamlc";
    protected static final String V_OCAML_VERSION_WSL = "4.12.0";
    protected static final String V_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Debian\\usr\\lib\\ocaml";

    // assuming that ocaml IS NOT installed on this path
    protected static final String I_OCAML_BINARY_WSL = "\\\\wsl$\\Debian\\home\\bin\\ocaml";
    protected static final String I_OCAML_COMPILER_WSL = "\\\\wsl$\\Debian\\home\\bin\\ocamlc";
    protected static final String I_OCAML_VERSION_WSL = "4.12.0";
    protected static final String I_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Debian\\home\\usr\\lib\\ocaml";

    // assuming that this path is "valid", but the WSL distribution is invalid
    protected static final String N_NAME_WSL = "Fedora";
    protected static final String N_OCAML_BINARY_WSL = "\\\\wsl$\\Fedora\\bin\\ocaml";
    protected static final String N_OCAML_COMPILER_WSL = "\\\\wsl$\\Fedora\\bin\\ocamlc";
    protected static final String N_OCAML_VERSION_WSL = "4.12.0";
    protected static final String N_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Fedora\\usr\\lib\\ocaml";

    // assuming that ocaml IS installed on this path
    protected static final String V_OCAML_BINARY_WIN = "C:\\cygwin64\\bin\\ocaml";
    protected static final String V_OCAML_COMPILER_WIN = "C:\\cygwin64\\bin\\ocamlc";
    protected static final String V_OCAML_VERSION_WIN = "4.10.0";
    protected static final String V_OCAML_SOURCES_FOLDER_WIN = "C:\\cygwin64\\lib\\ocaml";

    // assuming that ocaml IS NOT installed on this path
    protected static final String I_OCAML_BINARY_WIN = "C:\\cygwin\\bin\\ocaml";
    protected static final String I_OCAML_COMPILER_WIN = "C:\\cygwin\\bin\\ocamlc";
    protected static final String I_OCAML_VERSION_WIN = "4.10.0";
    protected static final String I_OCAML_SOURCES_FOLDER_WIN = "C:\\cygwin\\lib\\ocaml";

    private static final class CreateSdkParams {
        public String ocamlBinary;
        public String ocamlCompiler;
        public String version;
        public String ocamlSources;

        public CreateSdkParams(String ocamlBinary, String ocamlCompiler, String version, String ocamlSources) {
            this.ocamlBinary = ocamlBinary;
            this.ocamlCompiler = ocamlCompiler;
            this.version = version;
            this.ocamlSources = ocamlSources;
        }

        private static CreateSdkParams validWSL() {
            return new CreateSdkParams(V_OCAML_BINARY_WSL, V_OCAML_COMPILER_WSL, V_OCAML_VERSION_WSL, V_OCAML_SOURCES_FOLDER_WSL);
        }
        private static CreateSdkParams nonExistingWSL() {
            return new CreateSdkParams(N_OCAML_BINARY_WSL, N_OCAML_COMPILER_WSL, N_OCAML_VERSION_WSL, N_OCAML_SOURCES_FOLDER_WSL);
        }
        private static CreateSdkParams invalidWSL() {
            return new CreateSdkParams(I_OCAML_BINARY_WSL, I_OCAML_COMPILER_WSL, I_OCAML_VERSION_WSL, I_OCAML_SOURCES_FOLDER_WSL);
        }
        private static CreateSdkParams validWin() {
            return new CreateSdkParams(V_OCAML_BINARY_WIN, V_OCAML_COMPILER_WIN, V_OCAML_VERSION_WIN, V_OCAML_SOURCES_FOLDER_WIN);
        }
        private static CreateSdkParams invalidWin() {
            return new CreateSdkParams(I_OCAML_BINARY_WIN, I_OCAML_COMPILER_WIN, I_OCAML_VERSION_WIN, I_OCAML_SOURCES_FOLDER_WIN);
        }
    }

    public void assertConfigurationFailed(String expectedMessage, CreateSdkParams params) {
        assertThrows(ConfigurationException.class, expectedMessage, () -> OCamlSdkUtils.createSdk(
                params.ocamlBinary,
                params.version,
                params.ocamlCompiler,
                params.ocamlSources
        ));
    }
    public void assertConfigurationOK(CreateSdkParams params) {
        try {
            Sdk sdk = OCamlSdkUtils.createSdk(
                    params.ocamlBinary,
                    params.version,
                    params.ocamlCompiler,
                    params.ocamlSources
            );
            // todo: check SDK != null
            assertTrue(true);
        } catch (ConfigurationException e) {
            fail(e.getMessage());
        }
    }

    //
    // The following tests are checking that the parameters that are submitted
    // by the user are valid (ex: the binary).
    //
    // We must also ensure that there is test to check that the version is not empty
    // because this value is filled using an async process (=not immediate).
    //

    @Test
    public void testBinaryEmpty() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlBinary = "";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.binary.invalid"), params);
    }

    @Test
    public void testVersionEmpty() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.version = "";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.version.empty"), params);
    }

    //
    // An invalid binary does an ends with "/bin/ocaml" (resp. "/bin/ocaml", "/lib/ocaml")
    //
    // WSL: 1/5, the binary is invalid
    @Test
    public void testBinaryInvalidFormatWSL() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlBinary = "/bin/";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.binary.invalid"), params);
    }
    // WSL: 2/5, the sources' folder is invalid
    @Test
    public void testSourceInvalidFormatWSL() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlSources = "/lib/";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"), params);
    }
    // WSL: 3/5, every value is valid
    @Test
    public void testSourceValidFormatWSL() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        assertConfigurationOK(params);
    }
    // WSL: 4/5, every value is valid, but the WSL do not exist
    @Test
    public void testSourceInvalidWSL() {
        CreateSdkParams params = CreateSdkParams.nonExistingWSL();
        assertConfigurationFailed(
                OCamlBundle.message("sdk.path.binary.wsl.invalid", N_NAME_WSL), params
        );
    }
    // WSL: 5/5, valid, but sources' folder ends with a trailing slash
    @Test
    public void testSourceExtraFolderSeparatorWSL() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlSources += "/";
        assertConfigurationOK(params);
    }
    // Windows: 1/3, every value is valid
    @Test
    public void testBinaryInvalidFormatWin() {
        CreateSdkParams params = CreateSdkParams.validWin();
        params.ocamlBinary = "\\bin\\";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.binary.invalid"), params);
    }
    // Windows: 2/3, every value is valid
    @Test
    public void testSourceInvalidFormatWin() {
        CreateSdkParams params = CreateSdkParams.validWin();
        params.ocamlSources = "\\lib";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"), params);
    }
    // Windows: 3/4, every value is valid
    @Test
    public void testSourceValidFormatWin() {
        CreateSdkParams params = CreateSdkParams.validWin();
        assertConfigurationOK(params);
    }
    // Windows: 4/4, valid, but sources' folder ends with a trailing slash
    @Test
    public void testSourceExtraFolderSeparatorWin() {
        CreateSdkParams params = CreateSdkParams.validWin();
        params.ocamlSources += "\\";
        assertConfigurationOK(params);
    }
}
