package org.example

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL13.*
import org.lwjgl.system.MemoryUtil.NULL

fun main() {
    GLFWErrorCallback.createPrint(System.err).set()

    glfwInit()

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val window = glfwCreateWindow(800, 800, "Rodante", NULL, NULL)

    glfwMakeContextCurrent(window)

    GL.createCapabilities()

    while (!glfwWindowShouldClose(window)) {
        glfwPollEvents()

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)

        glfwSwapBuffers(window)
    }

    glfwTerminate()
}