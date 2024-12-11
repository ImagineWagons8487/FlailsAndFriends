package edu.csumb.flailsandfriends;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.csumb.flailsandfriends.entities.Equipment;

    public class EquipmentEntityUnitTest{
        private String name = "Axe", image = "axe.png";
        private int buff = 10;
        private Equipment equipment = new Equipment(name, image, buff);

        @Test
        public void equipmentNameTest(){
            String newName = "Spear";
            assertEquals(equipment.getEquipmentName(), name);
            equipment.setEquipmentName(newName);
            assertEquals(equipment.getEquipmentName(), newName);
        }

        @Test
        public void equipmentBitmapTest(){
            String newImage = "spear.png";
            assertEquals(equipment.getBitmapName(), image);
            equipment.setBitmapName(newImage);
            assertEquals(equipment.getBitmapName(), newImage);
        }

        @Test
        public void equipmentBuffTest(){
            int newBuff = 29;
            assertEquals(equipment.getBuff(), buff);
            equipment.setBuff(newBuff);
            assertEquals(equipment.getBuff(), newBuff);
        }
    }

