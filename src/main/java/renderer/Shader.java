package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-aA-z]+)");

            // Find all occurrences of substring [#type 'pattern']
            int matches = 1;
            while (matches < splitString.length) {
                int index = source.indexOf("#type") + "#type".length();
                int eol = source.indexOf("\r\n", index);
                String pattern = source.substring(index, eol).trim();
                switch (pattern) {
                    case "vertex":
                        if (vertexSource == null) {
                            vertexSource = splitString[1];
                            matches += 1;
                        } else {
                            throw new IOException("More then one of type: '" + pattern + "' defined in'" + filepath + "'");
                        }
                    case "fragment":
                        if (fragmentSource == null) {
                            fragmentSource = splitString[1];
                            matches += 1;
                        } else {
                            throw new IOException("More then one of type: '" + pattern + "' defined in'" + filepath + "'");
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
    }

    public void compile() {

    }

    public void use() {

    }

    public void detach() {

    }
}
