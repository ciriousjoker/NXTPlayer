# Table of contents

- [Introduction](#introduction)
- [NXTDisk Layout](#nxtdisk-layout)
- [Tone Data Layout](#tone-data-layout)
- [Meta Data Layout](#meta-data-layout)
- [Conversion Tables](#conversion-tables)
    - [Modifiers](#modifiers)
        - [Modifier](#modifier)
        - [Alt Modifier](#alt-modifier)
    - [Frequencies](#frequencies)
    - [Duration](#duration)


# Introduction
This lego robot can play back the [NXTDisks](#nxtdisk-layout) created with the [NXTBurner](https://github.com/CiriousJoker/NXTBurner).

# NXTDisk Layout

|   Color   |  Meaning  |              |
|-----------|-----------|--------------|
| ![#4caf50](https://placehold.it/15/4caf50/000000?text=+)<br>![#2196F3](https://placehold.it/15/2196F3/000000?text=+)<br>![#f44336](https://placehold.it/15/f44336/000000?text=+)<br>![#ffeb3b](https://placehold.it/15/ffeb3b/000000?text=+) | Meta data<br>1st row<br>2nd row<br>3rd row | <img src="https://cdn.rawgit.com/CiriousJoker/NXTPlayer/074bccfa/gallery/img/DiskStructure.svg" width="100%"> |


# Tone Data Layout

This is the data layout for one tone.

|           |        |              |
|-----------|--------|--------------|
| Frequency | Octave | Alt Modifier |
| Modifier  | Delay  | Duration     |

[Octave](#frequencies) and [Delay](#duration) are optional.

# Meta Data Layout

Note that the data uses base 9 for encoding.
For example, the bpm of 113 (base 9) translates to 93 bpm (81×1+9×1+1×3)

This is the data layout for the meta data:
<img src="https://cdn.rawgit.com/CiriousJoker/NXTPlayer/074bccfa/gallery/img/MetaDataLayout.svg" width="100%">

| Field              | Reason                                                                                                       |
|--------------------|--------------------------------------------------------------------------------------------------------------|
| Calibration fields | To account for the current lighting situation                                                                |
| Disk ID            | To (theoretically) rip disks while playing, so they can be played later. This feature was never implemented. |
| Base octave        | Setting a base octave allows for better compression because octave doesn't have to be changed on every note. |
| BPM                | Necessary to translate "full" notes and "quarters" into milliseconds.                                        |

# Conversion Tables

## Modifiers
There are a total of two modifiers. Two of them are needed to combine multiple features. For example, a series of 5 d# notes could be done by either printing 5 d# notes onto the disk (while using up 15 slots on the disk) or by settings the modifier to 7 and the alt modifier to 5.

### Modifier
| ID | Meaning                   |
|----|---------------------------|
| 0  | Skip the tone             |
| 1  | Play without modification |
| 2  | Loop marker               |
| 3  | Repeat 2 times            |
| 4  | Repeat 3 times            |
| 5  | Repeat 4 times            |
| 6  | Repeat 5 times            |
| 7  | Shift tone                |
| 8  | End of track              |

### Alt Modifier
| ID | Meaning                   |
|----|---------------------------|
| 0  | Unused                    |
| 1  | Play without modification |
| 2  | Repeat 2 times            |
| 3  | Repeat 3 times            |
| 4  | Repeat 4 times            |
| 5  | Repeat 5 times            |
| 6  | Repeat 6 times            |
| 7  | Repeat 7 times            |
| 8  | Skip the tone             |

# Frequencies

This table is taken from [pianotip.de](http://pianotip.de/frequenz.html). Big thanks to them for providing this list. All octaves from octave 0 to octave 7 are supported. Octave 5 is the base octave that's being used if nothing else is specified in either the tone or the meta data.

| Ton        | Hertz  | Ton         | Hertz  | Ton        | Hertz  | Ton        | Hertz    | Ton        | Hertz    |
|------------|--------|-------------|--------|------------|--------|------------|----------|------------|----------|
| **Octave** | **-1** |  **Octave** | **1**  | **Octave** | **3**  | **Octave** | **5**    | **Octave** | **7**    |
| ''A        | 27,5   | C           | 65,41  | c'         | 261,63 | c'''       | 1.046,50 | c'''''     | 4.186,01 |
| ''B        | 29,14  | Des         | 69,3   | des'       | 277,18 | des'''     | 1.108,73 |            |          |
| ''H        | 30,87  | D           | 73,42  | d'         | 293,66 | d'''       | 1.174,66 |            |          |
| **Octave** | **0**  | Es          | 77,78  | es'        | 311,13 | es'''      | 1.244,51 |            |          |
| 'C         | 32,7   | E           | 82,41  | e'         | 329,63 | e'''       | 1.318,51 |            |          |
| 'Des       | 34,65  | F           | 87,31  | f'         | 349,23 | f'''       | 1.396,91 |            |          |
| 'D         | 36,71  | Ges         | 92,5   | ges'       | 369,99 | ges'''     | 1.479,98 |            |          |
| 'Es        | 38,89  | G           | 98     | g'         | 392    | g'''       | 1.567,98 |            |          |
| 'E         | 41,2   | As          | 103,83 | as'        | 415,3  | as'''      | 1.661,22 |            |          |
| 'F         | 43,65  | A           | 110    | a'         | 440    | a'''       | 1.760,00 |            |          |
| 'Ges       | 46,25  | B           | 116,54 | b'         | 466,16 | b'''       | 1.864,66 |            |          |
| 'G         | 49     | H           | 123,47 | h'         | 493,88 | h'''       | 1.975,53 |            |          |
| 'As        | 51,91  | **Octave**  | **2**  | **Octave** | **4**  | **Octave** | **6**    |            |          |
| 'A         | 55     | c           | 130,81 | c''        | 523,25 | c''''      | 2.093,00 |            |          |
| 'B         | 58,27  | des         | 138,59 | des''      | 554,37 | des''''    | 2.217,46 |            |          |
| 'H         | 61,74  | d           | 146,83 | d''        | 587,33 | d''''      | 2.349,32 |            |          |
|            |        | es          | 155,56 | es''       | 622,25 | es''''     | 2.489,02 |            |          |
|            |        | e           | 164,81 | e''        | 659,26 | e''''      | 2.637,02 |            |          |
|            |        | f           | 174,61 | f''        | 698,46 | f''''      | 2.793,83 |            |          |
|            |        | ges         | 185    | ges''      | 739,99 | ges''''    | 2.959,96 |            |          |
|            |        | g           | 196    | g''        | 783,99 | g''''      | 3.135,96 |            |          |
|            |        | as          | 207,65 | as''       | 830,61 | as''''     | 3.322,44 |            |          |
|            |        | a           | 220    | a''        | 880    | a''''      | 3.520,00 |            |          |
|            |        | b           | 233,08 | b''        | 932,33 | b''''      | 3.729,31 |            |          |
|            |        | h           | 246,94 | h''        | 987,77 | h''''      | 3.951,07 |            |          |

## Duration
This is the tone duration chart I came up with. Note that a "Full" note isn't translatable to a number in milliseconds. It's always dependant on the current [BPM](https://en.wikipedia.org/wiki/BPM).

| ID | Duration       | Milliseconds     |
|----|----------------|------------------|
| 0  | Not played     | Not played       |
| 1  | Full           | 1000 * 240 / bpm |
| 2  | Half           | 1000 * 120 / bpm |
| 3  | Quarter        | 1000 * 60 / bpm  |
| 4  | Eighth         | 1000 * 30 / bpm  |
| 5  | Sixteenth      | 1000 * 15 / bpm  |
| 6  | Dotted Half    | 1000 * 180 / bpm |
| 7  | Dotted Quarter | 1000 * 90 / bpm  |
| 8  | Dotted Eighth  | 1000 * 45 / bpm  |