package com.ocaml.ide.sdk;

import com.intellij.openapi.options.*;
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
        private static CreateSdkParams invalidWSL() {
            return new CreateSdkParams(I_OCAML_BINARY_WSL, I_OCAML_COMPILER_WSL, I_OCAML_VERSION_WSL, I_OCAML_SOURCES_FOLDER_WSL);
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

    @Test
    public void testBinaryInvalidFormat() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlBinary = "/bin/";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.binary.invalid"), params);
    }

    @Test
    public void testSourceInvalidFormat() {
        CreateSdkParams params = CreateSdkParams.validWSL();
        params.ocamlSources = "/bin/";
        assertConfigurationFailed(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"), params);
    }

    @Test // the WSL do not exist
    public void testSourceInvalidWSL() {
        CreateSdkParams params = CreateSdkParams.invalidWSL();
        assertConfigurationFailed(OCamlBundle.message("sdk.path.binary.wsl.invalid", I_NAME_WSL), params);
    }
}
