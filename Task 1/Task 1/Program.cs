using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Task_1
{
    class Program
    {
        static bool isprime(int n) //making a function that finds only prime numbers
        {
            if (n == 1) return false; //because 1 isn't prime number
            for (int d = 2; d * d <= n; d++)
            {
                if (n % d == 0) return false; 
            }
            return true; //because in other cases it will be prime number
        }

        static void Main(string[] args)
        {
            int n = Int32.Parse(Console.ReadLine()); //input size of array
            int k = 0; //k will be a counter
            int[] a = new int[n]; //a will be array with size n

            for (int i = 0; i < n; i++)
            {
                a[i] = Int32.Parse(Console.ReadLine()); //input numbers (array)
            }

            for (int i = 0; i < n; i++) //go over the each number in the array
            {
                if (isprime(a[i])) //check if the numbers are prime in the array
                {
                    k++; //consider prime numbers
                }
            }

            Console.WriteLine(k); //write the amount of prime numbers

            for (int i = 0; i < n; i++) //go over the each number in the array
            {
                if (isprime(a[i])) //check if the numbers are prime in the array
                {
                    Console.Write(a[i] + " "); //if the numbers are prime, then output them
                }
            }
        }
    }
}
