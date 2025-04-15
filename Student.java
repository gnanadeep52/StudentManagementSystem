public class Student {
    private String id;
    private String name;
    private int age;

    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    // @Override
    // public String toString() {
    //     return "ID: " + id + ", Name: " + name + ", Age: " + age;
    // }
    public Object[] toRow() {
        return new Object[]{id, name, age};
    }
}
