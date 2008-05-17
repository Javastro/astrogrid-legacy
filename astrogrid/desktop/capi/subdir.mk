################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
./testintf.c 

CPP_SRCS += \
./intf.cpp \
./intfclasses.cpp 

OBJS += \
./intf.o \
./intfclasses.o \
./testintf.o 

C_DEPS += \
./testintf.d 

CPP_DEPS += \
./intf.d \
./intfclasses.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: %.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"./core" -I"./xmlrpc" -I"." -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -dynamic -v -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

%.o: %.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -I"./core" -I"." -I/Applications/itt/idl64/external/include -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


