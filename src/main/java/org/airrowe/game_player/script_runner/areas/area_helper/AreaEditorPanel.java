package org.airrowe.game_player.script_runner.areas.area_helper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListModel;

class AreaEditorPanel extends JPanel {

    private static final int HANDLE_SIZE = 6;
    private BufferedImage image;
    private final List<Rectangle> rectangles = new ArrayList<>();

    private Rectangle selected;
    private ResizeHandle activeHandle;
    private Point lastMouse;
    private Rectangle drawingRect;


    private ResizeHandle getHandle(Rectangle r, Point p) {
        Rectangle nw = new Rectangle(r.x - HANDLE_SIZE, r.y - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
        Rectangle ne = new Rectangle(r.x + r.width - HANDLE_SIZE, r.y - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
        Rectangle sw = new Rectangle(r.x - HANDLE_SIZE, r.y + r.height - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);
        Rectangle se = new Rectangle(r.x + r.width - HANDLE_SIZE, r.y + r.height - HANDLE_SIZE, HANDLE_SIZE * 2, HANDLE_SIZE * 2);

        if (nw.contains(p)) return ResizeHandle.NW;
        if (ne.contains(p)) return ResizeHandle.NE;
        if (sw.contains(p)) return ResizeHandle.SW;
        if (se.contains(p)) return ResizeHandle.SE;

        return null;
    }

    public AreaEditorPanel(BufferedImage image, DefaultListModel<Rectangle> listModel) {
        this.image = image;

        MouseAdapter mouse = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                lastMouse = e.getPoint();
                activeHandle = null;

                for (Rectangle r : rectangles) {
                    ResizeHandle h = getHandle(r, e.getPoint());
                    if (h != null) {
                        selected = r;
                        activeHandle = h;
                        return;
                    }
                    if (r.contains(e.getPoint())) {
                        selected = r;
                        repaint();
                        return;
                    }
                }

                // New rectangle
                selected = null;
                drawingRect = new Rectangle(e.getX(), e.getY(), 0, 0);
                listModel.addElement(drawingRect);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawingRect != null) {
                    drawingRect.setFrameFromDiagonal(lastMouse, e.getPoint());
                } else if (activeHandle != null && selected != null) {
                    resizeSelected(e.getPoint());
                } else if (selected != null) {
                    selected.translate(e.getX() - lastMouse.x, e.getY() - lastMouse.y);
                    lastMouse = e.getPoint();
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawingRect != null) {
                    rectangles.add(drawingRect);
                    selected = drawingRect;
                    drawingRect = null;
                }
                activeHandle = null;
                repaint();
            }
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    private void resizeSelected(Point p) {
        int dx = p.x - lastMouse.x;
        int dy = p.y - lastMouse.y;

        switch (activeHandle) {
            case NW -> {
                selected.x += dx;
                selected.y += dy;
                selected.width -= dx;
                selected.height -= dy;
            }
            case NE -> {
                selected.y += dy;
                selected.width += dx;
                selected.height -= dy;
            }
            case SW -> {
                selected.x += dx;
                selected.width -= dx;
                selected.height += dy;
            }
            case SE -> {
                selected.width += dx;
                selected.height += dy;
            }
        }
        lastMouse = p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);

        Graphics2D g2 = (Graphics2D) g;

        for (Rectangle r : rectangles) {
            g2.setColor(r == selected ? Color.YELLOW : Color.RED);
            g2.draw(r);

            if (r == selected) {
                drawHandles(g2, r);
            }
        }

        if (drawingRect != null) {
            g2.setColor(Color.GREEN);
            g2.draw(drawingRect);
        }
    }

    private void drawHandles(Graphics2D g2, Rectangle r) {
        g2.setColor(Color.WHITE);
        drawHandle(g2, r.x, r.y);
        drawHandle(g2, r.x + r.width, r.y);
        drawHandle(g2, r.x, r.y + r.height);
        drawHandle(g2, r.x + r.width, r.y + r.height);
    }

    private void drawHandle(Graphics2D g2, int x, int y) {
        g2.fillRect(x - HANDLE_SIZE / 2, y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public Rectangle getSelected() {
        return selected;
    }
}