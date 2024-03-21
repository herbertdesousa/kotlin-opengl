package org.example

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.*


val vertices: FloatArray = floatArrayOf(
    -0.5f, -0.5f, 0.0f, // left
    0.5f, -0.5f, 0.0f, // right
    0.0f, 0.5f, 0.0f  // top
)

val vertexShaderSource = "#version 330 core\n" +
        "layout (location = 0) in vec3 aPos;\n" +
        "void main()\n" +
        "{\n" +
        "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
        "}";

val fragmentShaderSource = "#version 330 core\n" +
        "out vec4 FragColor;\n" +
        "void main()\n" +
        "{\n" +
        "   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
        "}\n";

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

    val verticesBuffer = BufferUtils.createFloatBuffer(vertices.size)
    verticesBuffer.put(vertices).flip()

    val VBO = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, VBO)
    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * FLOAT_SIZE_IN_BYTES, 0)
    glEnableVertexAttribArray(0)

    while (!glfwWindowShouldClose(window)) {
        glfwPollEvents()

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        // rendering
        glUseProgram(shaderProgram)

        glBindVertexArray(VAO)

//        glEnableVertexAttribArray(0)

        glDrawArrays(GL_TRIANGLES, 0, 3)

//        glDisableVertexAttribArray(0)

//        glUseProgram(shaderProgram)
        //

        glfwSwapBuffers(window)
    }

    glfwTerminate()
}