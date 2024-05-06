package com.company

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.*


class Fractal : JPanel(true) {
    private var zoomFactor = 150.0
    private var centerX = 0.0
    private var centerY = 0.0
    private var fractalImage: BufferedImage? = null

    init {
        preferredSize = Dimension(600, 600)
        initializeMouseListener()
    }

    private fun initializeMouseListener() {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(event: MouseEvent) {
                centerX += (event.x - width / 2) / zoomFactor
                centerY += (event.y - height / 2) / zoomFactor
                zoomFactor *= 2
                updateFractal()
            }
        })
    }

    private fun updateFractal() {
        val width = width
        val height = height
        fractalImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until width) {
            for (y in 0 until height) {
                var zx = 0.0
                var zy = 0.0
                val cX = (x - width / 2) / zoomFactor + centerX
                val cY = (y - height / 2) / zoomFactor + centerY
                var iteration = 0
                var xTemp: Double

                while (zx * zx + zy * zy < 4 && iteration < 1000) {
                    xTemp = zx * zx - zy * zy + cX
                    zy = 2.0 * zx * zy + cY
                    zx = xTemp
                    iteration++
                }

                if (iteration < 1000) {
                    fractalImage?.setRGB(x, y, Color.HSBtoRGB(iteration / 256f, 1f, iteration / (iteration + 8f)))
                } else {
                    fractalImage?.setRGB(x, y, Color.BLACK.rgb)
                }
            }
        }
        repaint()
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        if (fractalImage != null) {
            graphics.drawImage(fractalImage, 0, 0, this)
        } else {

            println("Fractal image not available.")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                val frame = JFrame("Welcome to the Fractal Viewer!")
                frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                frame.isResizable = false
                val fractalPanel = Fractal()
                frame.add(fractalPanel, BorderLayout.CENTER)
                frame.pack()
                frame.setLocationRelativeTo(null)
                frame.isVisible = true
                fractalPanel.updateFractal()

                // Show custom message window
                val messageFrame = JFrame("CS636 - Welcome to the Fractal Viewer!")
                messageFrame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

                val messagePanel = JPanel()
                messagePanel.layout = BoxLayout(messagePanel, BoxLayout.Y_AXIS)
                messagePanel.preferredSize = Dimension(350, 150)
                messagePanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

                // Set background color of message frame
                messageFrame.contentPane.background = Color(240, 240, 240)
                val titleLabel = JLabel("<html><center><font size='5'>By: Tanishq Solanki  (01958002)</font></center></html>")
                titleLabel.alignmentX = Component.CENTER_ALIGNMENT

                val infoLabel = JLabel("<html><center>Please click wherever you want to zoom.<br>You can close this window now.</center></html>")
                infoLabel.alignmentX = Component.CENTER_ALIGNMENT

                val closeButton = JButton("Close")
                closeButton.alignmentX = Component.CENTER_ALIGNMENT
                closeButton.addActionListener { messageFrame.dispose() }

                messagePanel.add(titleLabel)
                messagePanel.add(Box.createRigidArea(Dimension(0, 10)))
                messagePanel.add(infoLabel)
                messagePanel.add(Box.createRigidArea(Dimension(0, 10)))
                messagePanel.add(closeButton)

                messageFrame.add(messagePanel)
                messageFrame.pack()
                messageFrame.setLocationRelativeTo(frame)
                messageFrame.isVisible = true
            }
        }
    }
}
