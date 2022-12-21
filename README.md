# EntityCount
Given a perimeter defined by two xyz coordinates and an entity type, command /mobcount will give in return the total of block, spawnable block, entity of that type and ratio spawnable block per entity.

e.g: /mobcount -3 61 -14 -5 61 17 COW

You can save a zone: /mobcount -3 61 -14 -5 61 17 COW set ZoneNameToSave

then to use it: /mobcount zone ZoneNameToUse

You can delete a saved zone: /mobcount del ZoneNameToDelete

And last but not least you can get the list of recorded zone /mobcount list

In addition you have also a zone definition by having a Kelp in your principal hand and right click two block at the two coordinates.

Implemented types: any entity by using the entity_id (e.g: cow, creeper, cat, zombie_pigman,...)
Tips: Typing an entity that does not exist will not filtering on type and will return the full count of living entity in the zone.