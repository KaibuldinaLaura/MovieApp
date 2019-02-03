using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Task_3
{
    class Program
    {
        static void output(int b)
        {
            Console.Write(b + " " + b + " ");
        }
        static void Main(string[] args)
        {
            int n = Int32.Parse(Console.ReadLine()); //input size of array
            int[] a = new int[n]; //array a with size n
            for (int i = 0; i < n; i++)
            {
                a[i] = Int32.Parse(Console.ReadLine()); //input numbers (array)
            }
            foreach (int number in a)
            {
                output(number);
            }
        }
    }
}
