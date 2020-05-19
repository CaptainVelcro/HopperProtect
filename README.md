# HopperProtect

HopperProtect solves CoreProtect's biggest issue. It is recommended that HopperProtect is used with CoreProtect but since CoreProtect is not a dependency this is not required.

## The Problem

CoreProtect doesn't log when hoppers or hopper minecarts extract items from inventories. Therefore, someone could place a hopper or hopper minecart under a chest and there would be no way to find out who did it. (CoreProtect logs block placements, so a hopper would be detected but a hopper minecart would not since it is an entity.)

## The Solution

Users can lock any inventory so that hoppers and hopper minecarts can't extract from it. To lock an inventory, the user has to look at an inventory and execute the /lock command. If the inventory was already locked by the same user, the inventory will be unlocked. If the inventory was locked by a different user, nothing happens.

### Extra Information

- HopperProtect does not prevent locked inventories from being broken. CoreProtect will log all block breaks.
- All placed inventories are unlocked by default.

### Other Features

- Double chest locking and unlocking happens automatically. If one chest is locked/unlocked, the other one will be too.
- If a locked inventory is broken and placed back in the same spot, it will not be locked.
- More to come soon.
