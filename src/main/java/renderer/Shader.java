package renderer;

import java.io.IOException;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '"+filepath+"'";
        }
    }

    public void compile() {

    }

    public void use() {

    }

    public void detach() {

    }
}
