package com.nopalsoft.ninjarunner.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pools
import com.nopalsoft.ninjarunner.Assets
import com.nopalsoft.ninjarunner.Assets.playSound
import com.nopalsoft.ninjarunner.Settings.selectedSkin
import com.nopalsoft.ninjarunner.objects.*
import com.nopalsoft.ninjarunner.objects.Item.Companion.DURATION_PICK
import com.nopalsoft.ninjarunner.objects.Player.Companion.DURATION_DEAD

class WorldGame {
    var state: Int = 0
    var myWorldBox: World = World(Vector2(0f, -9.8f), true)
    var myPlayer: Player? = null
    var myPet: Pet? = null
    var timeToSpawnMissile: Float
    var myManager: ObjectManagerBox2d
    var arrayBodies: Array<Body>
    var arrayPlatforms: Array<Platform>
    var arrayWall: Array<Wall>
    var arrayItems: Array<Item>
    var arrayObstacles: Array<Obstacle>
    var arrayMissiles: Array<Missile>

    /**
     * Variable that indicates how far the world has been created.
     */
    var worldCreatedUpToX: Float

    var takenCoins: Int = 0
    var score: Long = 0
    var isBodySliding: Boolean = false // Indicates whether the body you have right now is sliding.
    var recreateFixture: Boolean = false

    init {
        myWorldBox.setContactListener(Collisions())

        myManager = ObjectManagerBox2d(this)

        arrayBodies = Array()
        arrayPlatforms = Array()
        arrayItems = Array()
        arrayWall = Array()
        arrayObstacles = Array()
        arrayMissiles = Array()

        timeToSpawnMissile = 0f

        myManager.createHeroStand(2f, 1f, selectedSkin)
        myManager.createPet(myPlayer!!.position.x - 1, myPlayer!!.position.y + .75f)

        worldCreatedUpToX = myManager.createPlatform(0f, 0f, 3)

        createNextPart()
    }

    private fun createNextPart() {
        val x = worldCreatedUpToX

        while (worldCreatedUpToX < (x + 1)) {
            // First I create the platform

            val separation = MathUtils.random(1f, 3f)
            val y = MathUtils.random(0f, 1.5f)

            // y = 0;
            val connectedPlatforms = 25

            worldCreatedUpToX = myManager.createPlatform(worldCreatedUpToX + separation, y, connectedPlatforms)

            // Then I add things above the platform.
            var xAux = x + separation

            while (xAux < worldCreatedUpToX - 2) {
                if (xAux < x + separation + 2) xAux = addRandomItems(xAux, y)

                if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createBox4(xAux, y + .8f)
                    xAux = addRandomItems(xAux, y)
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createBox7(xAux, y + 1f)
                    xAux = addRandomItems(xAux, y)
                } else if (MathUtils.randomBoolean(.1f)) {
                    xAux = myManager.createWall(xAux, y + 3.17f)
                    xAux = addRandomItems(xAux, y)
                } else {
                    xAux = addRandomItems(xAux, y)
                }
            }
        }
    }

    private fun addRandomItems(xAux: Float, y: Float): Float {
        var xAux = xAux
        if (MathUtils.randomBoolean(.3f)) {
            for (i in 0..4) {
                myManager.createItem(ItemCurrency::class.java, xAux, y + 1.5f)
                xAux = myManager.createItem(ItemCurrency::class.java, xAux, y + 1f)
            }
        } else if (MathUtils.randomBoolean(.5f)) {
            for (i in 0..4) {
                myManager.createItem(ItemCandyBean::class.java, xAux, y + .8f)
                myManager.createItem(ItemCandyBean::class.java, xAux, y + 1.1f)
                xAux = myManager.createItem(ItemCandyJelly::class.java, xAux, y + 1.5f)
            }
        } else if (MathUtils.randomBoolean(.5f)) {
            for (i in 0..4) {
                myManager.createItem(ItemCandyCorn::class.java, xAux, y + .8f)
                myManager.createItem(ItemCandyCorn::class.java, xAux, y + 1.1f)
                xAux = myManager.createItem(ItemCandyCorn::class.java, xAux, y + 1.5f)
            }
        }

        if (MathUtils.randomBoolean(.025f)) {
            xAux = myManager.createItem(ItemHearth::class.java, xAux, y + 1.5f)
            xAux = myManager.createItem(ItemEnergy::class.java, xAux, y + 1.5f)
        } else if (MathUtils.randomBoolean(.025f)) {
            xAux = myManager.createItem(ItemMagnet::class.java, xAux, y + 1.5f)
        }

        return xAux
    }

    fun update(delta: Float, didJump: Boolean, dash: Boolean, didSlide: Boolean) {
        myWorldBox.step(delta, 8, 4)

        myWorldBox.getBodies(arrayBodies)
        eliminarObjetos()
        myWorldBox.getBodies(arrayBodies)

        for (body in arrayBodies) {
            if (body.userData is Player) {
                updatePersonaje(delta, body, didJump, dash, didSlide)
            } else if (body.userData is Pet) {
                updateMascota(delta, body)
            } else if (body.userData is Platform) {
                updatePlataforma(body)
            } else if (body.userData is Wall) {
                updatePared(body)
            } else if (body.userData is Item) {
                updateItem(delta, body)
            } else if (body.userData is Obstacle) {
                updateObstaculos(delta, body)
            } else if (body.userData is Missile) {
                updateMissil(delta, body)
            }
        }

        if (myPlayer!!.position.x > worldCreatedUpToX - 5) createNextPart()

        if (myPlayer!!.state == Player.STATE_DEAD && myPlayer!!.stateTime >= DURATION_DEAD) state = STATE_GAMEOVER

        timeToSpawnMissile += delta
        val TIME_TO_SPAWN_MISSIL = 15f
        if (timeToSpawnMissile >= TIME_TO_SPAWN_MISSIL) {
            timeToSpawnMissile -= TIME_TO_SPAWN_MISSIL

            myManager.createMissile1(myPlayer!!.position.x + 10, myPlayer!!.position.y)
        }
    }

    private fun eliminarObjetos() {
        for (body in arrayBodies) {
            if (body.userData is Platform) {
                val obj = body.userData as Platform
                if (obj.state == Platform.STATE_DESTROY) {
                    arrayPlatforms.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            } else if (body.userData is Wall) {
                val obj = body.userData as Wall
                if (obj.state == Wall.STATE_DESTROY) {
                    arrayWall.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            } else if (body.userData is Item) {
                val obj = body.userData as Item
                if (obj.state == Item.STATE_DESTROY && obj.stateTime >= DURATION_PICK) {
                    arrayItems.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            } else if (body.userData is ObstacleBoxes4) {
                val obj = body.userData as ObstacleBoxes4

                if (obj.effect != null && obj.state == Obstacle.STATE_DESTROY && obj.effect!!.isComplete) {
                    obj.effect!!.free()
                    arrayObstacles.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            } else if (body.userData is ObstacleBoxes7) {
                val obj = body.userData as ObstacleBoxes7

                if (obj.effect != null && obj.state == Obstacle.STATE_DESTROY && obj.effect!!.isComplete) {
                    obj.effect!!.free()
                    arrayObstacles.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            } else if (body.userData is Missile) {
                val obj = body.userData as Missile
                if (obj.state == Missile.STATE_DESTROY) {
                    arrayMissiles.removeValue(obj, true)
                    Pools.free(obj)
                    myWorldBox.destroyBody(body)
                }
            }
        }
    }

    private fun updatePersonaje(delta: Float, body: Body, didJump: Boolean, dash: Boolean, didSlide: Boolean) {
        myPlayer!!.update(delta, body, didJump, false, dash, didSlide)

        if (myPlayer!!.position.y < -1) {
            myPlayer!!.die()
        } else if (myPlayer!!.isSlide && !isBodySliding) {
            recreateFixture = true
            isBodySliding = true
            myManager.recreateFixturePlayerSlide(body)
        } else if (!myPlayer!!.isSlide && isBodySliding) {
            recreateFixture = true
            isBodySliding = false
            myManager.recreateFixturePlayerStand(body)
        }
    }

    private fun updateMascota(delta: Float, body: Body) {
        var targetPositionX = myPlayer!!.position.x - .75f
        var targetPositionY = myPlayer!!.position.y + .25f

        if (myPet!!.petType == Pet.PetType.BOMB) {
            val oMissile = closestUpcomingMissile
            if (oMissile != null && oMissile.distanceFromCharacter < 5f && oMissile.state == Missile.STATE_NORMAL) {
                targetPositionX = oMissile.position.x
                targetPositionY = oMissile.position.y
            }
        } else {
            if (myPlayer!!.isDash) {
                targetPositionX = myPlayer!!.position.x + 4.25f
                targetPositionY = myPlayer!!.position.y
            }
        }

        myPet!!.update(body, delta, targetPositionX, targetPositionY)
    }

    private fun updatePlataforma(body: Body) {
        val obj = body.userData as Platform

        if (obj.position.x < myPlayer!!.position.x - 3) obj.setDestroy()
    }

    private fun updatePared(body: Body) {
        val obj = body.userData as Wall

        if (obj.position.x < myPlayer!!.position.x - 3) obj.setDestroy()
    }

    private fun updateItem(delta: Float, body: Body) {
        val obj = body.userData as Item
        obj.update(delta, body, myPet!!, myPlayer!!)

        if (obj.position.x < myPlayer!!.position.x - 3) obj.setPicked()
    }

    private fun updateObstaculos(delta: Float, body: Body) {
        val obj = body.userData as Obstacle
        obj.update(delta)

        if (obj.position.x < myPlayer!!.position.x - 3) obj.setDestroy()
    }

    private fun updateMissil(delta: Float, body: Body) {
        val obj = body.userData as Missile
        obj.update(delta, body, myPlayer!!)

        if (obj.position.x < myPlayer!!.position.x - 3) obj.setDestroy()

        arrayMissiles.sort()
    }

    private val closestUpcomingMissile: Missile?
        /**
         * Return the missile closest to the character that is in normal state.
         */
        get() {
            for (i in 0 until arrayMissiles.size) {
                if (arrayMissiles[i].state == Missile.STATE_NORMAL) return arrayMissiles[i]
            }
            return null
        }


    internal inner class Collisions : ContactListener {
        override fun beginContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) beginContactHeroOtraCosa(a, b)
            else if (b.body.userData is Player) beginContactHeroOtraCosa(b, a)

            if (a.body.userData is Pet) beginContactMascotaOtraCosa(b)
            else if (b.body.userData is Pet) beginContactMascotaOtraCosa(a)
        }

        private fun beginContactHeroOtraCosa(fixHero: Fixture, otraCosa: Fixture) {
            val other = otraCosa.body.userData

            if (other is Platform) {
                if (fixHero.userData == "pies") {
                    if (recreateFixture) recreateFixture = false
                    else myPlayer!!.touchFloor()
                }
            } else if (other is Item) {
                val obj = other
                if (obj.state == Item.STATE_NORMAL) {
                    if (obj is ItemCurrency) {
                        takenCoins++
                        score++
                        playSound(Assets.coin!!, 1)
                    } else if (obj is ItemMagnet) {
                        myPlayer!!.setPickUpMagnet()
                    } else if (obj is ItemEnergy) {
                        //todo oPlayer.shield++;
                    } else if (obj is ItemHearth) {
                        myPlayer!!.lives++
                    } else if (obj is ItemCandyJelly) {
                        playSound(Assets.popCandy!!, 1)
                        score += 2
                    } else if (obj is ItemCandyBean) {
                        playSound(Assets.popCandy!!, 1)
                        score += 5
                    } else if (obj is ItemCandyCorn) {
                        playSound(Assets.popCandy!!, 1)
                        score += 15
                    }

                    obj.setPicked()
                }
            } else if (other is Wall) {
                if (other.state == Wall.STATE_NORMAL) {
                    myPlayer!!.getDizzy()
                }
            } else if (other is Obstacle) {
                val obj = other
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setDestroy()
                    myPlayer!!.getHurt()
                }
            } else if (other is Missile) {
                val obj = other
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget()
                    myPlayer!!.getDizzy()
                }
            }
        }

        fun beginContactMascotaOtraCosa(otherFixture: Fixture) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Wall && myPlayer!!.isDash) {
                otherObject.setDestroy()
            } else if (otherObject is Obstacle && myPlayer!!.isDash) {
                otherObject.setDestroy()
            } else if (otherObject is ItemCurrency) {
                val obj = otherObject
                if (obj.state == Item.STATE_NORMAL) {
                    obj.setPicked()
                    takenCoins++
                    playSound(Assets.coin!!, 1)
                }
            } else if (otherObject is Missile) {
                val obj = otherObject
                if (obj.state == Obstacle.STATE_NORMAL) {
                    obj.setHitTarget()
                }
            }
        }

        override fun endContact(contact: Contact) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a == null || b == null) return

            if (a.body.userData is Player) endContactHeroOtherFixture(a, b)
            else if (b.body.userData is Player) endContactHeroOtherFixture(b, a)
        }

        private fun endContactHeroOtherFixture(fixHero: Fixture, otherfixture: Fixture) {
            val otherObject = otherfixture.body.userData

            if (otherObject is Platform) {
                if (fixHero.userData == "pies") myPlayer!!.endTouchFloor()
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val a = contact.fixtureA
            val b = contact.fixtureB

            if (a.body.userData is Player) preSolveHero(a, b, contact)
            else if (b.body.userData is Player) preSolveHero(b, a, contact)
        }

        private fun preSolveHero(fixHero: Fixture, otherFixture: Fixture, contact: Contact) {
            val otherObject = otherFixture.body.userData

            if (otherObject is Platform) {
                val ponyY = fixHero.body.position.y - .30f
                val pisY = otherObject.position.y + Platform.HEIGHT / 2f

                if (ponyY < pisY) contact.isEnabled = false
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            // TODO Auto-generated method stub
        }
    }

    companion object {
        const val STATE_GAMEOVER: Int = 1
    }
}
