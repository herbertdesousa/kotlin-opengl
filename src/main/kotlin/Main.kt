package org.example

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.*


val vertices: FloatArray = floatArrayOf(
    // position         // color
    0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f, 1.0f,   // Bottom right 0
    -0.5f, 0.5f, 0.0f,  0.0f, 1.0f, 0.0f, 1.0f,   // Top left     1
    0.5f, 0.5f, 0.0f,   1.0f, 0.0f, 1.0f, 1.0f,  // Top right    2
    -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,    // Bottom left  3
)

val indices = intArrayOf(
    2, 1, 0, // Top right triangle
    0, 1, 3, // bottom left triangle
)

val vertexShaderSource = """
    #version 330 core
    
    layout (location = 0) in vec3 aPos;
    layout (location = 1) in vec4 aColor;
    
    out vec4 fColor;
    
    void main() 
    {
        fColor = aColor;
        gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
    }
"""

val fragmentShaderSource = """
    #version 330 core
    
    in vec4 fColor;
    out vec4 color;
    
    void main() 
    {
        color = fColor;
    }
"""

const val POSITION_SIZE = 3
const val COLOR_SIZE = 4

const val FLOAT_SIZE_IN_BYTES = 4

fun main() {
    GLFWErrorCallback.createPrint(System.err).set()

    glfwInit()

    glfwDefaultWindowHints()
//    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
//    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
//    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val window = glfwCreateWindow(800, 800, "Rodante", NULL, NULL)

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1);
    glfwShowWindow(window);
    GL.createCapabilities()

    // shader program
    val vertexShader = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertexShader, vertexShaderSource)
    glCompileShader(vertexShader)

    var success = glGetShaderi(vertexShader, GL_COMPILE_STATUS)
    if (success == GL_FALSE) {
        val len = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH)
        println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.")
        println(glGetShaderInfoLog(vertexShader, len))

        assert(false) { "" }
    }

    val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragmentShader, fragmentShaderSource)
    glCompileShader(fragmentShader)

    success = glGetShaderi(vertexShader, GL_COMPILE_STATUS)
    if (success == GL_FALSE) {
        val len = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH)
        println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.")
        println(glGetShaderInfoLog(vertexShader, len))

        assert(false) { "" }
    }

    val shaderProgram = glCreateProgram()
    glAttachShader(shaderProgram, vertexShader)
    glAttachShader(shaderProgram, fragmentShader)
    glLinkProgram(shaderProgram)

    success = glGetProgrami(shaderProgram, GL_LINK_STATUS)
    if (success == GL_FALSE) {
        val len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)
        println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.")
        println(glGetProgramInfoLog(shaderProgram, len))

        assert(false) { "" }
    }

    glDeleteShader(vertexShader)
    glDeleteShader(fragmentShader)

    // setting shader up
    val VAO = glGenVertexArrays()
    glBindVertexArray(VAO)

    val VBO = glGenBuffers()
    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
    verticesBuffer.put(vertices).flip()

    glBindBuffer(GL_ARRAY_BUFFER, VBO)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)

    val EBO = glGenBuffers()
    val indicesBuffer = BufferUtils.createIntBuffer(indices.size)
    indicesBuffer.put(indices).flip()

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

    val strideSize = (POSITION_SIZE + COLOR_SIZE) * FLOAT_SIZE_IN_BYTES

    glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, strideSize, 0)
    glEnableVertexAttribArray(0)

    glVertexAttribPointer(
        1,
        COLOR_SIZE,
        GL_FLOAT,
        false,
        strideSize,
        (POSITION_SIZE * FLOAT_SIZE_IN_BYTES).toLong()
    )
    glEnableVertexAttribArray(1)

//    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

    while (!glfwWindowShouldClose(window)) {
        glfwPollEvents()

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        // rendering
        glUseProgram(shaderProgram)

        glBindVertexArray(VAO)

//        glEnableVertexAttribArray(0)

        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)

//        glDisableVertexAttribArray(0)

//        glUseProgram(shaderProgram)
        //

        glfwSwapBuffers(window)
    }

    glfwTerminate()
}