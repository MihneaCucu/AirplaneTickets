# Proiect Java "Airplane Reservation System"

## Descriere
Acest proiect Java este un sistem de rezervări pentru aeronave, destinat gestionării zborurilor, aeroporturilor, aeronavelor și pasagerilor. Proiectul include funcționalități de bază pentru adăugarea, modificarea și afișarea entităților, precum și gestionarea rezervărilor și locurilor.

## Structură proiect
- **Domain**: Clasele de bază (ex: Aircraft, Airport, Flight, Passenger etc.)
- **DAO**: Acces la date pentru fiecare entitate (ex: AircraftDAO, FlightDAO)
- **Services**: Logica de business și operații principale (ex: FlightService, AirportService)
- **Utils**: Utilitare și enum-uri (ex: AircraftType)

## Funcționalități principale
- Adăugare, modificare și afișare aeronave
- Gestionare aeroporturi (adăugare, modificare, afișare)
- Gestionare zboruri (adăugare, modificare, afișare)
- Gestionare pasageri și rezervări
- Afișare locuri disponibile și rezervate pentru fiecare zbor
- Căutare zboruri după aeroporturi și date
- Serviciu de audit


## Exemple de utilizare
- Adăugare zbor nou
- Listare pasageri pentru un zbor
- Modificare date aeroport

## Structura bazei de date

- **Aircraft**: ID, AircraftType, TotalSeats
- **Airport**: ID, Name, City, Country
- **Flights**: FlightNumber, DepartureAirport, ArrivalAirport, DepartureDateTime, ArrivalDateTime, Aircraft
- **Seats**: ID, Row, Position, ExtraLegroom, PassengerName, FlightID

