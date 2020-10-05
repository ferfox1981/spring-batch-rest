# Objective
The objetive of this project is to contruct a database of COVID data focused on presenting the Moving Average of COVID related deaths on states of Brazil.

# Idea

1) Create a scheduled service that will collect the COVID data of the last X days.
2) Store this information in a NOSQL database calculating the Moving Average
3) (Future) Create a page containing graphs of the moving average

# Deployment & Scheduling (Cron)

The system will be deployed in a Docker-like free hosting service (maybe Heroku)

# Data Source

We are going to use the following public API to collect day-to-day inforation about COVID deaths
Data Source: https://covid19-brazil-api.now.sh/status
We can compare our number with this site:
https://ciis.fmrp.usp.br/covid19/pe-br/

# Storage

I'm considering to use Firebase to store the information.

# Construction

I've decided to use Java 1.8 + Spring Batch as a development language.

# Promotion
An article on Medium will be written detailing the development


