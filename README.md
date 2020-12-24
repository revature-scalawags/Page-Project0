# kepler Data Reader
CLI application built for the purpose of parsing and filtering data from Nasa's , Kepler project. 

## Description
Nasa's kepler project was a years long endeavour that set out to discover and record data on exoplanets within the Milky Way. Over its operation period, 10s of thousands of planets were discovered.  The Kepler Data Reader loads a csv file (downloaded from Nasa's webpage). User's can generate a new and more personalized table with columns of their choosing.  A new CSV file is generated.  And that table is loaded into a noSQL database.  Once Their,  constraints queries can be applied.  For example, we can find all exoplanets with an equilibrium of 400k or lower. 


## Tech Used and Required
Scala and SBT: (https://www.scala-lang.org/download/2.12.8.html).    
JDK (v11): (https://jdk.java.net/15/).    
MongoDB: org.mongodb.scala

## Project:
Repo: (https://github.com/revature-scalawags/Page-Project0)    
My Github: (https://github.com/drthisguy)    
Email: (page.c.tyler@gmail.com)    

## Know Issues:
- Occasionally, when a connection to the database is slow, the may timeout, and/or throw an exception. 

- If any others, are discovered, please feel free to contact me.  Cheers. 
