package org.rone.core.util.hibernate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自关联
 * @author Rone
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    /**
     * 父亲
     */
    @ManyToOne
    @JoinColumn(name = "father_id")
    private User father;

    /**
     * 孩子
     */
    @OneToMany(mappedBy = "father", targetEntity = User.class, cascade = CascadeType.ALL)
    private List<User> childs = new ArrayList<>();

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", father=" + (father!=null ? father.getName() : null) +
                ", childs=" + childs +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getFather() {
        return father;
    }

    public void setFather(User father) {
        this.father = father;
    }

    public List<User> getChilds() {
        return childs;
    }

    public void setChilds(List<User> childs) {
        this.childs = childs;
    }
}
