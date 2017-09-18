#!/usr/bin/python3

class Employee:
    'Common base class for all employees'
    empCount = 0

    def __init__(self, name, salary, age):
        self.name = name
        self.salary = salary
        self.age = age
        Employee.empCount += 1

    def displayCount(self):
        print("Total Employee %d" % Employee.empCount)

    def displayEmployee(self):
        print("Name : ", self.name, ", Salary: ", self.salary, "age: ", self.age)

"This would create first object of Employee class"
emp1 = Employee("Zara", 2000, 3)
"This would create second object of Employee class"
emp2 = Employee("Manni", 5000, 9)
emp1.age = 7  # Add an 'age' attribute.
emp1.age = 8  # Modify 'age' attribute.
#del emp1.age  # Delete 'age' attribute.

hasattr(emp1, 'age')
getattr(emp1, 'age')
#delattr(emp1, 'age')

emp1.displayEmployee()
emp2.displayEmployee()
print("Total Employee %d" % Employee.empCount)

print("Employee.__doc__:", Employee.__doc__)
print("Employee.__name__:", Employee.__name__)
print("Employee.__module__:", Employee.__module__)
print("Employee.__bases__:", Employee.__bases__)
print("Employee.__dict__:", Employee.__dict__)


print("")
print(" --- This is new test ---")
print("")

class Parent:        # define parent class
   parentAttr = 1000
   def __init__(self):
      print("Calling parent constructor")

   def parentMethod(self):
      print('Calling parent method')

   def setAttr(self, attr):
      Parent.parentAttr = attr

   def getAttr(self):
      print("Parent attribute :", Parent.parentAttr)


class Child(Parent): # define child class
   def __init__(self):
      print("Calling child constructor")

   def childMethod(self):
      print('Calling child method')


c = Child()          # instance of child
c.childMethod()      # child calls its method
c.parentMethod()     # calls parent's method
c.setAttr(200)       # again call parent's method
c.getAttr()          # again call parent's method

print("")
print(" --- This is new test ---")
print("")


class Parent:        # define parent class
   def myMethod(self):
      print('Calling parent method')


class Child(Parent): # define child class
   def myMethod(self):
      print('Calling child method')

c = Child()          # instance of child
c.myMethod()         # child calls overridden method

print("")
print(" --- This is new test ---")
print("")


class Vector:
    def __init__(self, a, b):
        self.a = a
        self.b = b

    def __str__(self):
        return 'Vector (%d, %d)' % (self.a, self.b)

    def __add__(self, other):
        return Vector(self.a + other.a, self.b + other.b)


v1 = Vector(2, 10)
v2 = Vector(5, -2)
v3 = Vector(10, 10)
print(v1 + v2 + v3)


print("")
print(" --- This is new test ---")
print("")


class JustCounter:
    __secretCount = 1

    def count(self):
        self.__secretCount *= 2
        print(self.__secretCount)


counter = JustCounter()
counter.count()
counter.count()
counter.count()
counter.count()
print(counter.__secretCount)

print("")
print(" --- This is new test ---")
print("")
