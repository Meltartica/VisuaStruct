package com.visustruct;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Tree {
    private Stage stage;
    private double horizontalSpacing;
    private double verticalSpacing = 100;
    private final BinaryTree tree = new BinaryTree();

    @FXML
    private AnchorPane anchorPane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void backHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("datastructures.fxml"));
            Parent root = loader.load();

            DataStructuresMenu dsmenu = loader.getController();
            dsmenu.setStage(stage);
            stage.setTitle("VisuaStruct - Data Structures Menu");

            Scene currentScene = stage.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        showInitialNodePopup();

        anchorPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                double x = e.getX();
                double y = e.getY();
                Node clickedNode = tree.findNode(x, y);

                if (clickedNode != null) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Delete Node");
                    alert.setHeaderText("Are you sure you want to delete this node?");
                    ButtonType deleteButton = new ButtonType("Delete");
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
                    alert.getButtonTypes().setAll(deleteButton, cancelButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == deleteButton) {
                        tree.deleteNode(clickedNode);
                        updateTree();

                        if (tree.countNodes() == 0) {
                            showInitialNodePopup();
                        }
                    }
                }
            } else if (e.getButton() == MouseButton.PRIMARY) {
                double x = e.getX();
                double y = e.getY();
                Node clickedNode = tree.findNode(x, y);

                if (clickedNode != null) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Insert Node");
                    alert.setHeaderText("Insert a node:");
                    ButtonType leftButton = new ButtonType("Left");
                    ButtonType middleButton = new ButtonType("Middle");
                    ButtonType rightButton = new ButtonType("Right");
                    ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
                    alert.getButtonTypes().setAll(leftButton, middleButton, rightButton, cancelButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() != cancelButton) {
                        if (result.get() == leftButton) {
                            showNodeValuePopup("Left", clickedNode);
                        } else if (result.get() == rightButton) {
                            showNodeValuePopup("Right", clickedNode);
                        } else if (result.get() == middleButton) {
                            showNodeValuePopup("Middle", clickedNode);
                        }
                    }
                }
            }
        });

        anchorPane.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                zoomInButton();
            } else {
                zoomOutButton();
            }
        });
    }

    private void showInitialNodePopup() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Initial Node");
        dialog.setHeaderText("Enter the value for the root node:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(value -> {
            tree.setRoot(new Node(400, 50, value));
            updateTree();
        });
    }

    private void showNodeValuePopup(String position, Node parent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Node");
        dialog.setHeaderText("Enter the value for the " + position + " child node:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(value -> {
            switch (position) {
                case "Left" -> tree.insertLeft(parent, value);
                case "Right" -> tree.insertRight(parent, value);
                case "Middle" -> tree.insertMiddle(parent, value);
            }
            updateTree();
        });
    }

    private void updateTree() {
        anchorPane.getChildren().clear();
        tree.draw(anchorPane);
    }

    public void zoomInButton() {
        double scaleFactor = 1.1;
        anchorPane.setScaleX(anchorPane.getScaleX() * scaleFactor);
        anchorPane.setScaleY(anchorPane.getScaleY() * scaleFactor);
    }

    public void zoomOutButton() {
        double scaleFactor = 1.1;
        anchorPane.setScaleX(anchorPane.getScaleX() / scaleFactor);
        anchorPane.setScaleY(anchorPane.getScaleY() / scaleFactor);
    }

    public void clearHandler(ActionEvent actionEvent) {
        tree.setRoot(null);
        updateTree();
        showInitialNodePopup();
    }

    public void nodeCountHandler(ActionEvent actionEvent) {
        int nodeCount = tree.countNodes();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Node Count");
        alert.setHeaderText("Total Number of Nodes: " + nodeCount);
        alert.showAndWait();
    }

    public static class Node {
        double x, y;
        String value;
        Node left, middle, right;

        Node(double x, double y, String value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    public class BinaryTree {
        private Node root;

        public void setRoot(Node node) {
            root = node;
        }

        private void calculateSpacing(int level) {
            horizontalSpacing = 400 / (Math.pow(2, level));

            verticalSpacing = 100;
        }

        public void insertLeft(Node parent, String value) {
            if (parent.left == null) {
                int level = getNodeLevel(parent);
                calculateSpacing(level + 1);
                parent.left = new Node(parent.x - horizontalSpacing - 10, parent.y + verticalSpacing, value);
            }
        }

        public void insertRight(Node parent, String value) {
            if (parent.right == null) {
                int level = getNodeLevel(parent);
                calculateSpacing(level + 1);
                parent.right = new Node(parent.x + horizontalSpacing + 10, parent.y + verticalSpacing, value);
            }
        }

        public void insertMiddle(Node parent, String value) {
            if (parent.middle == null) {
                int level = getNodeLevel(parent);
                calculateSpacing(level + 1);
                parent.middle = new Node(parent.x, parent.y + verticalSpacing, value);
            }
        }

        public void deleteNode(Node target) {
            root = deleteNode(root, target);
        }

        private Node deleteNode(Node current, Node target) {
            if (current == null) {
                return null;
            }

            if (current == target) {
                return null;
            }

            current.left = deleteNode(current.left, target);
            current.middle = deleteNode(current.middle, target);
            current.right = deleteNode(current.right, target);

            return current;
        }

        public Node findNode(double x, double y) {
            return findNode(root, x, y);
        }

        private Node findNode(Node current, double x, double y) {
            if (current == null) {
                return null;
            }

            if (Math.abs(current.x - x) < 20 && Math.abs(current.y - y) < 20) {
                return current;
            }

            Node leftResult = findNode(current.left, x, y);
            if (leftResult != null) {
                return leftResult;
            }

            Node middleResult = findNode(current.middle, x, y);
            if (middleResult != null) {
                return middleResult;
            }

            return findNode(current.right, x, y);
        }

        private int getNodeLevel(Node node) {
            return getNodeLevel(root, node, 0);
        }

        private int getNodeLevel(Node current, Node target, int level) {
            if (current == null) {
                return -1;
            }

            if (current == target) {
                return level;
            }

            int leftLevel = getNodeLevel(current.left, target, level + 1);
            if (leftLevel != -1) {
                return leftLevel;
            }

            int middleLevel = getNodeLevel(current.middle, target, level + 1);
            if (middleLevel != -1) {
                return middleLevel;
            }

            return getNodeLevel(current.right, target, level + 1);
        }

        public int countNodes() {
            return countNodes(root);
        }

        private int countNodes(Node node) {
            if (node == null) {
                return 0;
            }

            int count = 1;
            count += countNodes(node.left);
            count += countNodes(node.middle);
            count += countNodes(node.right);

            return count;
        }

        public void draw(Pane anchorPane) {
            drawTree(root, Tree.this.anchorPane);
        }

        private void drawTree(Node node, Pane anchorPane) {
            if (node == null) {
                return;
            }

            if (node.left != null) {
                Tree.this.anchorPane.getChildren().add(new Line(node.x, node.y, node.left.x, node.left.y));
                drawTree(node.left, Tree.this.anchorPane);
            }

            if (node.right != null) {
                Tree.this.anchorPane.getChildren().add(new Line(node.x, node.y, node.right.x, node.right.y));
                drawTree(node.right, Tree.this.anchorPane);
            }

            if (node.middle != null) {
                Tree.this.anchorPane.getChildren().add(new Line(node.x, node.y, node.middle.x, node.middle.y));
                drawTree(node.middle, Tree.this.anchorPane);
            }

            Circle circle = new Circle(node.x, node.y, 20);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            Tree.this.anchorPane.getChildren().add(circle);

            Text text = new Text(node.x - 5, node.y + 5, node.value);
            text.setFill(Color.BLACK);
            Tree.this.anchorPane.getChildren().add(text);
        }
    }
}