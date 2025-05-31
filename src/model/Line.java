package model;

import structures.*;

public class Line {
    
    private String name;
    private BinaryTree<Integer> models;

    public Line(String name){
        this.name = name;
        this.models = new BinaryTree<Integer>(new IntegerComparator());
    }

    public void addModel(Integer model){
        models.add(model);
    }

    public BinaryTree<Integer> getModels(){
        return models;
    }

    public String getName() {
        return name;
    }
}
