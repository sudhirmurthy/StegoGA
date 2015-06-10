
			int[] solutionArray = s.getIntegerEncodedBytes();
			System.out.println("Unsigned Byte Decode is:  ");
			for(int sl = 0;sl<solutionArray.length;sl++){
				System.out.print(" ");
				System.out.print(solutionArray[sl]);
			}
			
			System.out.println("\nConverted to 255 byte is: ");
			for(int i=0;i<solutionArray.length;i++){
				solutionArray[i] = (solutionArray[i] & 0xff);
				//iBytes[i] = (iBytes[i] & 0xff);
				System.out.print(" ");
				System.out.print(solutionArray[i]);
			}
			
			byte[] b = new byte[solutionArray.length];
			for(int i=0;i<b.length;i++){
				b[i] = (byte)solutionArray[i];
			}
			System.out.println("\nUnsigned Byte Decode is: ");
			for(int i=0;i<b.length;i++){
				solutionArray[i] = b[i];
				System.out.print(" ");
				System.out.print(solutionArray[i]);
			}
		
			