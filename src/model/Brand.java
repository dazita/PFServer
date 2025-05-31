package model;

import structures.*;

public class Brand {

    private String name;
    private BinaryTree<Line> lines;

    public Brand(String name){
        this.name = name;
        this.lines = new BinaryTree<Line>(new LineComparator());
    }

    public void addLine(Line line){
        lines.add(line);
    }

    public BinaryTree<Line> getLines(){
        return lines;
    }

    public String getName() {
        return name;
    }
}