# Titulo

@startuml
skinparam classAttributeIconSize 0
class Animal {
  - nombre:String
  - alimento:String
  - edad:int
  - raza:String
  
  + Animal(...)
  + Alimentarse():void
}


class Perro
class Gato
class Caballo
Animal <|-- Perro
Animal <|-- Gato
Animal <|-- Caballo
@enduml
