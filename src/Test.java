public class Test {


    static class Human{
        public String sex;
        public String name;
        public int  age;

        public void eat(){
            System.out.println(name + "正在吃东西");
        }

        public Human(String sex, String name, int age) {
            this.sex = sex;
            this.name = name;
            this.age = age;
        }
    }


    public static void main(String[] args) {
        Human human = new Human("男","小明",12);
        human.eat();
    }
}
