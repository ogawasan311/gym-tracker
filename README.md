# gym-tracker

A Java command-line tool that logs your sets and instantly shows the weight you used last session.

## The Problem

Every time I'm at the gym, I have to dig through my phone's notes app to find how much weight I used during my last session. For example, if I want to use the leg extension machine, I have to find how much weight I used last time for my sets. Essentially, it is a faster way to look up information without interrupting the workout.

## How It Works

- `log` records a set (exercise, weight, reps, date) to a local CSV file
- `lookup` prints the last weight and reps for an exercise
- `history` shows recent sets and your best set

![System diagram](docs/finalsystemdiagram.drawio.png)

## Tech Stack

- **Language:** Java 25
- **Libraries:** Java standard library (`java.io`, `java.time`)
- **Data storage:** local CSV file, no external services

## Setup

```bash
git clone https://github.com/ogawasan311/gym-tracker
cd gym-tracker
# build and run commands go here once the code exists
```

## Usage

(Add real commands and sample output once the program works.)

## Roadmap

- [ ] Progressive overload suggestions
- [ ] Edit or delete an incorrect entry
- [ ] Auto-recommend exercise based on history
- [ ] Mobile front end for an app


## Author

Sanshiro Ogawa ([@ogawasan311](https://github.com/ogawasan311))