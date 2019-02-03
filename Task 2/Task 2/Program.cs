using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Task_2
{
    class Student
    {
        public string name;
        public string id;

        public Student(string name, string id)
        {
            this.name = name;
            this.id = id;
        }

        public void PrintInfo()
        {
            Console.WriteLine(name);
            Console.WriteLine(id);
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Student student = new Student("Omarov", "11BD111111");
            int a = 1;
            student.PrintInfo();
            Console.WriteLine($"The next year of study is: {a+1}");
        }
    }
}